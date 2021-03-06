package edu.stanford.bmir.protege.web.server.icd.proposals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

import au.com.bytecode.opencsv.CSVReader;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * The class that does the actual import of the proposal into iCAT. Proposals
 * are processed row by row.
 * 
 * @author ttania
 *
 */
public class ImportProposals {

	private String user;
	private OWLModel owlModel;

	private UploadProposalsResponse response;
	private ImportResult importResult;

	public ImportProposals(OWLModel owlModel, String user) {
		this.owlModel = owlModel;
		this.user = user;
		this.response = new UploadProposalsResponse();
		this.importResult = new ImportResult();
	}

	public UploadProposalsResponse importProposals(File proposalsFile) {
		processFile(proposalsFile);
		new ImportResultWriter(importResult).writeImportOutput(response);
		return response;
	}

	private void processFile(File proposalsFile) {
		long t0 = System.currentTimeMillis();
		Log.getLogger().info("Started import of ICD proposals on " + new Date());

		int count = 0;
		CSVReader reader = null;
		boolean eventGenerationEnabled = true;
		try {
			// disable event generation, so that the clients do not get hundreds or thousand
			// of events at the same time; big performance impact for the client
			eventGenerationEnabled = owlModel.setGenerateEventsEnabled(false);
			reader = new CSVReader(new FileReader(proposalsFile), '|');
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				count++;
				try {
					processLine(nextLine);
				} catch (Exception e) {
					Log.getLogger().log(Level.WARNING, "Could not process line no. " + count, e);
				}

				if (count % 100 == 0) {
					Log.getLogger().info("Imported " + count + " ICD proposals. Date: " + new Date());
				}
			}
		} catch (IOException e) {
			Log.getLogger().log(Level.WARNING,
					"Error at accessing ICD proposals CSV file: " + proposalsFile.getAbsolutePath(), e);
			response.setResponse(500, "Error at accessing the ICD proposals CSV file on the server filesystem.");
			return;
		} finally {
			// renable events
			owlModel.setGenerateEventsEnabled(eventGenerationEnabled);
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.getLogger().log(Level.WARNING,
							"Error at closing the ICD proposals CSV file reader: " + proposalsFile.getAbsolutePath(),
							e);
					response.setResponse(500, "Error at closing the ICD proposals CSV file reader.");
					return;
				}
			}
		}

		long importTime = (System.currentTimeMillis() - t0) / 1000;

		int successRowCount = importResult.getSuccessRowCount();
		int ignoreRowCount = importResult.getIgnoreRowCount();
		int failRowCount = importResult.getFailRowCount();
		Log.getLogger()
				.info("Ended import of ICD proposals on " + new Date() + ". Processed " + count
						+ " lines. Import took: " + importTime + " seconds.\n" + "Success rows: " + successRowCount
						+ " Ignored rows: " + ignoreRowCount + " Failed rows: " + failRowCount);
		response.setResponse(200,
				"Processed " + count + " lines. \n" + "Success rows: " + successRowCount + ". \n" + "Ignored rows: "
						+ ignoreRowCount + ". \n" + "Failed rows: " + failRowCount + ". \n" + "Import took "
						+ importTime + " seconds. \n\n" + "Date: " + new Date());
	}

	private void processLine(String[] values) {
		String contributionId = getValue(values, 0);
		String contributableId = getValue(values, 1);
		String entityId = getValue(values, 2);
		String entityPublicId = getValue(values, 3);
		String contributorFullName = getValue(values, 4);
		String entryDateTime = getValue(values, 5);
		String status = getValue(values, 6);
		String rationale = getValue(values, 7);
		String proposalType = getValue(values, 8);
		String proposalGroupId = getValue(values, 9);
		String url = getValue(values, 10);
		String propertyId = getValue(values, 11);
		String oldValue = getValue(values, 12);
		String newValue = getValue(values, 13);
		String idFromValueSet = getValue(values, 14);
		String valueSetName = getValue(values, 15);

		if (ImportProposalsUtil.isAccepted(status) == false) {
			importResult.recordResult(contributionId, "Proposal status is not 'Accepted'.", ImportRowStatus.IGNORED);
			return;
		}

		ICDProposal proposal = getProposal(owlModel, contributionId, contributableId, entityId, entityPublicId, contributorFullName, 
				entryDateTime, status, rationale, proposalType, proposalGroupId, url, propertyId, 
				oldValue, newValue, idFromValueSet, valueSetName);
		
		if (proposal == null) {
			Log.getLogger().warning("Unrecognized proposal type: " + proposalType);
			importResult.recordResult(contributionId, "Unrecognized proposal type: " + proposalType, ImportRowStatus.FAIL);
			return;
		}
		
		proposal.doImport(user, importResult);
		
	}
	

	private String getValue(String[] values, int i) {
		return i < values.length ? values[i] : null;
	}

	private ICDProposal getProposal(OWLModel owlModel, String contributionId, String contributableId,
			String entityId, String entityPublicId, String contributorFullName, String entryDateTime,
			String status, String rationale, String proposalType,
			String proposalGroupId, String url, String propertyId,
			String oldValue, String newValue, String idFromValueSet,
			String valueSetName) {
		
		
		if ( ProposalTypes.AddContentProposal.toString().equals(proposalType) ||
				//Public platform exception: definition and title have always edit proposals,
				//even if it is an add. In that case, the contributableId is NA.
				( ProposalTypes.EditContentProposal.toString().equals(proposalType) && 
				  ImportProposalsUtil.getNAString().equals(contributableId)) ) {
			return ICDProposalFactory.createAddContentProposal(owlModel, contributionId, contributableId, 
						entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
						proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
						
		} else if (ProposalTypes.EditContentProposal.toString().equals(proposalType)) {
			return ICDProposalFactory.createEditContentProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
					
		} else if (ProposalTypes.DeleteContentProposal.toString().equals(proposalType)) {
			return ICDProposalFactory.createDeleteContentProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
			
		} else if (ProposalTypes.CreateSubclassProposal.toString().equals(proposalType)) {
			return ICDProposalFactory.createCreateSubclassProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
			
		} 
		
		/************* Post Coordination ***********/
		
		else if (ProposalTypes.AddAllowedPostCoordinationAxisProposal.toString().equals(proposalType)) {
			return ICDProposalFactory.createAddAllowedPostCoordinationAxisProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
			
		} else if (ProposalTypes.AddRequiredPostCoordinationAxisProposal.toString().equals(proposalType)) {
			return ICDProposalFactory.createAddRequiredPostCoordinationAxisProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
			
		} else if (ProposalTypes.RemovePostCoordinationAxisProposal.toString().equals(proposalType)) {
			return ICDProposalFactory.createRemovePostCoordinationAxisProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
			
		} else if (ProposalTypes.AddCustomScaleProposal.toString().equals(proposalType)) {
//TODO
//			return ICDProposalFactory.createEditContentProposal(owlModel, contributionId, contributableId, 
//					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
//					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
			
		} else if (ProposalTypes.DeleteCustomScaleProposal.toString().equals(proposalType)) {
//TODO
//			return ICDProposalFactory.createEditContentProposal(owlModel, contributionId, contributableId, 
//					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
//					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
			
		} else if (ProposalTypes.AddPostCoordinationAxisValueProposal.toString().equals(proposalType)) {
			return ICDProposalFactory.createAddPostCoordinationAxisValueProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
			
		} else if (ProposalTypes.DeletePostCoordinationAxisValueProposal.toString().equals(proposalType)) {
			return ICDProposalFactory.createDeletePostCoordinationAxisValueProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName);
		
		} 
		
		/************* Logical defintions ***********/
		
		else if (ProposalTypes.AddLogicalDefinition.toString().equals(proposalType)) {
			return ICDProposalFactory.createAddLogicalDefinitionProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, 
					valueSetName, true);
		
		} else if (ProposalTypes.EditLogicalDefinition.toString().equals(proposalType)) {
			return ICDProposalFactory.createEditLogicalDefinitionProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, 
					valueSetName, true);
		
		} else if (ProposalTypes.DeleteLogicalDefinition.toString().equals(proposalType)) {
			return ICDProposalFactory.createDeleteLogicalDefinitionProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet,
					valueSetName, true);
		
		}  else if (ProposalTypes.AddLogicalDefinitionNonDef.toString().equals(proposalType)) {
			return ICDProposalFactory.createAddLogicalDefinitionProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, 
					valueSetName, false);
		
		} else if (ProposalTypes.EditLogicalDefinitionNonDef.toString().equals(proposalType)) {
			return ICDProposalFactory.createEditLogicalDefinitionProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, 
					valueSetName, false);
		
		} else if (ProposalTypes.DeleteLogicalDefinitionNonDef.toString().equals(proposalType)) {
			return ICDProposalFactory.createDeleteLogicalDefinitionProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, 
					valueSetName, false);
		}
		
		
		return null;
	}

}
