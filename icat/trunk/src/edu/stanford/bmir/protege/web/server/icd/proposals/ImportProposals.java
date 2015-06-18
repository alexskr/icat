package edu.stanford.bmir.protege.web.server.icd.proposals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * The class that does the actual import of the proposal into iCAT.
 * Proposals are processed row by row.
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
				
		return response;
	}

	private void processFile(File proposalsFile) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(proposalsFile));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					if (line != null) {
						try {
							processLine(line);
						} catch (Exception e) {
							Log.getLogger().log(Level.WARNING," Could not read line: " + line, e);
						}
					}
				}				
			} catch (IOException e) {
				Log.getLogger().log(Level.WARNING, "Error at parsing csv file: " + proposalsFile.getAbsolutePath(), e);
				return;
			} finally {
				if (input != null) {
					input.close();
				}
			}
		} catch (IOException ex) {
			Log.getLogger().log(Level.WARNING, "Error at accessing csv file: " + proposalsFile.getAbsolutePath(), ex);
		}
	}

	private void processLine(String line) {
		String[] values = line.split("\\|");
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
		
		if (ProposalTypes.AddContent.toString().equals(proposalGroupId)) {
				ICDProposalFactory.createAddContentProposal(owlModel, contributionId, contributableId, 
						entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
						proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName).
						doImport(user, importResult);
				
		} else if (ProposalTypes.EditContent.toString().equals(proposalGroupId)) {
			ICDProposalFactory.createEditContentProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName).
					doImport(user, importResult);
			
		} else if (ProposalTypes.DeleteContent.toString().equals(proposalGroupId)) {
			ICDProposalFactory.createDeleteContentProposal(owlModel, contributionId, contributableId, 
					entityId, entityPublicId, contributorFullName, entryDateTime, status, rationale, 
					proposalType, proposalGroupId, url, propertyId, oldValue, newValue, idFromValueSet, valueSetName).
					doImport(user, importResult);
			
		} else {
			Log.getLogger().warning("Unrecognized proposal type: " + proposalType);
			importResult.recordResult(contributionId, "Unrecognized proposal type: " + proposalType, ImportRowStatus.FAIL);			
		}
	}


	private String getValue(String[] values, int i) {
		return i < values.length ? removeQuotes(values[i]) : null;
	}

	private String removeQuotes(String str) {
		if (str == null) {
			return null;
		}
		String ret = str.trim();
		if (str.startsWith("\"") && str.endsWith("\"")) {
			ret = str.substring(1, str.length() - 1);
		}
		return ret.trim();
	}

	
}
