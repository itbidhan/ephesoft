/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.gwt.rv.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.gwt.core.client.event.AnimationCompleteEvent;
import com.ephesoft.dcma.gwt.core.client.event.AnimationCompleteEventHandler;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.util.GWTListBoxControl;
import com.ephesoft.dcma.gwt.core.client.util.GWTValidatableControl;
import com.ephesoft.dcma.gwt.core.client.util.ReverseIterable;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.DocumentRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.FuzzySearchEvent;
import com.ephesoft.dcma.gwt.rv.client.event.FuzzySearchEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TableViewDisplayEvent;
import com.ephesoft.dcma.gwt.rv.client.event.TableViewDisplayEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.ValidationFieldChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

@SuppressWarnings("deprecation")
public class ValidatePanel extends RVBasePanel {

	private static final String ENABLE_SCRIPT_VALIDATION = "ON";

	interface Binder extends UiBinder<DockLayoutPanel, ValidatePanel> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	private final Button fuzzySearchBtn;

	private final Button showTableViewBtn;

	private final TextBox fuzzySearchText;

	private final ValidatableWidget<TextBox> fuzzySearchTextBox;

	@UiField
	FlexTable validationTable;

	@UiField
	ScrollPanel scrollPanel;

	@UiField
	FlexTable fuzzySearchTable;

	private Boolean showErrorField = Boolean.TRUE;

	private Boolean isPrevious = Boolean.FALSE;

	private final static String ALTERNATE_STRING_VALUE = LocaleDictionary.get().getConstantValue(
			ReviewValidateConstants.alternate_value);
	private final static String SEPERATOR = ReviewValidateConstants.seperator;

	private List<List<String>> fuzzyDataCarrier;

	private final String enableSwitchScriptValidation = ENABLE_SCRIPT_VALIDATION;

	public ValidatePanel() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		scrollPanel.addStyleName(ReviewValidateConstants.OVERFLOW_SCROLL);
		fuzzySearchBtn = new Button();
		showTableViewBtn = new Button();
		showTableViewBtn.setStyleName("tableViewButton");
		showTableViewBtn.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_table_view_tooltip));
		fuzzySearchText = new TextBox();
		fuzzySearchBtn.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.fuzzy_search_go_btn));
		fuzzySearchBtn.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.fuzzy_search_tooltip));
		fuzzySearchBtn.setStyleName("fuzzySearchButton");

		fuzzySearchTextBox = new ValidatableWidget<TextBox>(fuzzySearchText);
		fuzzySearchTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fuzzySearchTextBox.toggleValidDateBox();
				fuzzySearchTextBox.getWidget().addStyleName("validatePanelListBox");
			}
		});
		// fuzzySearchTextBox.addValidator(new EmptyStringValidator(fuzzySearchText));
		fuzzySearchTextBox.getWidget().addStyleName("validatePanelListBox");
		fuzzySearchText.addKeyboardListener(new KeyboardListenerAdapter() {

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == (char) KEY_ENTER) {
					fuzzySearchBtn.click();
				}
			}
		});

		showTableViewBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				presenter.setTableView(Boolean.TRUE);
				presenter.onTableViewButtonClicked();
			}
		});

		fuzzySearchBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				ScreenMaskUtility.maskScreen();
				String value = fuzzySearchText.getValue();
				if (null == value || value.trim().isEmpty()) {
					final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
					confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.msg_fuzzy_search_invalid_entry));
					confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
							ReviewValidateConstants.fuzzy_search_tooltip));
					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							confirmationDialog.hide();
							ScreenMaskUtility.unmaskScreen();
							presenter.setFocus();
						}

						@Override
						public void onCancelClick() {
							ScreenMaskUtility.unmaskScreen();
							presenter.setFocus();
						}
					});
					confirmationDialog.center();
					confirmationDialog.show();
					confirmationDialog.okButton.setFocus(true);
				} else {

					presenter.rpcService.fuzzyTextSearch(presenter.batchDTO.getBatch(), value,
							new AsyncCallback<List<List<String>>>() {

								@Override
								public void onFailure(Throwable arg0) {
									final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
									confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
											ReviewValidateMessages.msg_fuzzy_search_unsuccessful));
									confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
											ReviewValidateConstants.fuzzy_search_tooltip));
									confirmationDialog.addDialogListener(new DialogListener() {

										@Override
										public void onOkClick() {
											confirmationDialog.hide();
											ScreenMaskUtility.unmaskScreen();
											presenter.setFocus();
										}

										@Override
										public void onCancelClick() {
											ScreenMaskUtility.unmaskScreen();
											presenter.setFocus();
										}
									});
									confirmationDialog.center();
									confirmationDialog.show();
									confirmationDialog.okButton.setFocus(true);
								}

								@Override
								public void onSuccess(List<List<String>> arg0) {

									if (arg0 == null || arg0.isEmpty()) {
										final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
										confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
												ReviewValidateMessages.msg_fuzzy_search_no_result));
										confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
												ReviewValidateConstants.fuzzy_search_tooltip));
										confirmationDialog.addDialogListener(new DialogListener() {

											@Override
											public void onOkClick() {
												confirmationDialog.hide();
												ScreenMaskUtility.unmaskScreen();
												presenter.setFocus();
											}

											@Override
											public void onCancelClick() {
												ScreenMaskUtility.unmaskScreen();
												presenter.setFocus();
											}

										});
										confirmationDialog.center();
										confirmationDialog.show();
										confirmationDialog.okButton.setFocus(true);
									} else {
										fuzzyDataCarrier = arg0;

										final DialogBox dialogBox = new DialogBox();
										dialogBox.addStyleName("overflow_scroll");
										HorizontalPanel panel = new HorizontalPanel();
										panel.setWidth("100%");
										panel.setHeight("100%");
										final FuzzySearchResultView fuzzySearchResultView = new FuzzySearchResultView(arg0.get(0),
												presenter);
										fuzzySearchResultView.setEventBus(eventBus);
										dialogBox.setWidth("100%");
										dialogBox.setHeight("100%");
										panel.add(fuzzySearchResultView);
										dialogBox.setWidget(panel);
										fuzzySearchResultView.setDialogBox(dialogBox);
										fuzzySearchResultView.createBatchList(arg0);
										dialogBox.center();
										dialogBox.setText(LocaleDictionary.get().getConstantValue(
												ReviewValidateConstants.fuzzy_search_title));
										dialogBox.show();
										fuzzySearchResultView.getSelectBtn().setFocus(true);
									}
								}

							});
				}
				fuzzySearchText.setText("");
			}
		});

	}

	public void refreshPanel(Document document) {
		this.presenter.document = document;
		this.validationTable.clear();
		presenter.rpcService.getFieldTypeDTOs(document.getType(), presenter.batchDTO.getBatch().getBatchInstanceIdentifier(),
				new AsyncCallback<List<FieldTypeDTO>>() {

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.error_ret_batch));
						// return;
					}

					@Override
					public void onSuccess(List<FieldTypeDTO> arg0) {
						setView(arg0);
						presenter.setFocus();
					}
				});

	}

	private void setFocus1() {
		// Boolean focusSet = Boolean.FALSE;
		boolean focusWasSet = false;
		if (isPrevious) {
			if (showErrorField) {
				ReverseIterable<DocFieldWidget> iterator = new ReverseIterable<DocFieldWidget>(docFieldWidgets);
				for (DocFieldWidget docFieldWid : iterator) {
					if (docFieldWid.widget != null && !docFieldWid.widget.validate()) {
						docFieldWid.widget.getWidget().setFocus(true);
						focusWasSet = true;
						docFieldWid.setCurrent(true);
						// focusSet = Boolean.TRUE;
						break;
					} else if (docFieldWid.lWidget != null && !validateListBoxSelection(docFieldWid.lWidget)) {
						docFieldWid.lWidget.setFocus(true);
						focusWasSet = true;
						docFieldWid.setCurrent(true);
						// focusSet = Boolean.TRUE;
						break;
					}
				}
			}
		} else {
			if (showErrorField) {
				for (DocFieldWidget docFieldWidget : docFieldWidgets) {
					if (docFieldWidget.widget != null && !docFieldWidget.widget.validate()) {
						docFieldWidget.widget.getWidget().setFocus(true);
						docFieldWidget.setCurrent(true);
						focusWasSet = true;
						// focusSet = Boolean.TRUE;
						break;
					} else if (docFieldWidget.lWidget != null && !validateListBoxSelection(docFieldWidget.lWidget)) {
						docFieldWidget.lWidget.setFocus(true);
						docFieldWidget.setCurrent(true);
						focusWasSet = true;
						// focusSet = Boolean.TRUE;
						break;
					}
				}

			}
		}
		if (!focusWasSet && docFieldWidgets != null && !docFieldWidgets.isEmpty() && docFieldWidgets.get(0) != null) {
			docFieldWidgets.get(0).setCurrent(true);
			if (docFieldWidgets.get(0).widget != null) {
				docFieldWidgets.get(0).widget.getWidget().setFocus(true);
			} else {
				docFieldWidgets.get(0).lWidget.setFocus(true);
			}
		}
	}

	public boolean setFocusAfterConformationDialog() {
		boolean returnVal = false;
		if (docFieldWidgets != null && !docFieldWidgets.isEmpty()) {
			setFocus1();
			returnVal = true;
		}
		return returnVal;
	}

	// private void setVisibility() {
	// switch (presenter.batchDTO.getBatch().getBatchStatus()) {
	// case READY_FOR_REVIEW:
	// this.setVisible(false);
	// clearPanel();
	// break;
	// case READY_FOR_VALIDATION:
	// this.setVisible(true);
	// break;
	// default:
	// GWT.log("Unknown Status for validate Panel.");
	// break;
	// }
	// }

	public void clearPanel() {
		this.validationTable.clear();
		this.docFieldWidgets.clear();
		this.fuzzySearchTable.setVisible(false);
	}

	public void setView(List<FieldTypeDTO> fieldTypeDTOs) {
		// This field is used to hide document level field on validate panel

		boolean isFieldHidden = false;
		// setVisibility();
		clearPanel();
		if (!this.isVisible()) {
			return;
		}
		fuzzySearchTable.setVisible(true);
		Label fuzzyLabel = new Label("Fuzzy Search : ");
		fuzzyLabel.setWidth("105px");
		fuzzyLabel.addStyleName("bold_text");
		fuzzySearchTable.getCellFormatter().setWidth(0, 0, "115px");
		fuzzySearchTable.setWidget(0, 0, fuzzyLabel);
		fuzzySearchTable.setWidget(1, 0, fuzzySearchText);
		fuzzySearchTable.setWidget(1, 1, fuzzySearchBtn);
		fuzzySearchTable.setWidget(1, 2, showTableViewBtn);
		fuzzySearchTable.getCellFormatter().setWidth(1, 1, "20%");
		fuzzySearchTable.getCellFormatter().setWidth(1, 2, "80%");
		Label seperator = new Label(".");
		seperator.addStyleName("fuzzy-seperator");
		// fuzzySearchTable.setWidget(2, 0, seperator);
		int index = 1;
		List<DocField> documentLevelFields = presenter.document.getDocumentLevelFields().getDocumentLevelField();
		if (null != documentLevelFields && !documentLevelFields.isEmpty()) {
			Collections.sort(documentLevelFields, new Comparator<DocField>() {

				public int compare(DocField fieldOne, DocField fieldSec) {
					int compare = 0;
					if (fieldOne != null && fieldSec != null) {
						int fieldOrderNumberOne = fieldOne.getFieldOrderNumber();
						int fieldOrderNumberSec = fieldSec.getFieldOrderNumber();
						if (fieldOrderNumberOne > fieldOrderNumberSec) {
							compare = 1;
						} else {
							if (fieldOrderNumberOne < fieldOrderNumberSec) {
								compare = -1;
							} else {
								compare = 0;
							}
						}
					}
					return compare;
				};
			});
			for (final DocField field : documentLevelFields) {

				Map<String, Integer> alternateValues = new HashMap<String, Integer>();
				field.getName();
				if (field.getAlternateValues() != null) {
					for (Field alternateField : field.getAlternateValues().getAlternateValue()) {
						if (alternateValues.containsKey(alternateField.getValue())) {
							int nextVal = alternateValues.get(alternateField.getValue()).intValue();
							nextVal++;
							alternateValues.remove(alternateField.getValue());
							alternateValues.put(alternateField.getValue(), Integer.valueOf(nextVal));
						} else {
							alternateValues.put(alternateField.getValue(), 0);
						}
					}
				}
				Iterator<Map.Entry<String, Integer>> it = alternateValues.entrySet().iterator();
				List<String> alternateValuesSet = new ArrayList<String>();
				while (it.hasNext()) {
					Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
					if (pair.getValue().intValue() != 0) {
						for (int k = 0; k <= pair.getValue().intValue(); k++) {
							alternateValuesSet.add(pair.getKey() + SEPERATOR + ALTERNATE_STRING_VALUE + k);
						}
					} else {
						alternateValuesSet.add(pair.getKey());
					}
				}
				String tempFieldName = "";
				String sampleVString = "";
				String fieldTypeDescription = null;
				List<String> values = new ArrayList<String>();
				ValidatableWidget<SuggestBox> tempVWidget = null;
				boolean isShowListBox = false;
				ListBox tempListBox = null;
				for (FieldTypeDTO fieldTypeDTO : fieldTypeDTOs) {
					if (!fieldTypeDTO.isHidden()) {
						if (fieldTypeDTO.getName().equals(field.getName())) {
							isFieldHidden = false;
							String fieldOptionValueList = fieldTypeDTO.getFieldOptionValueList();

							if (fieldOptionValueList != null && !fieldOptionValueList.isEmpty()
									&& !fieldOptionValueList.matches("^[ ;]{1,}")) {
								isShowListBox = true;
								values = Arrays.asList(fieldTypeDTO.getFieldOptionValueList().split(";"));
								tempListBox = GWTListBoxControl.createGWTListControl(field.getType(), field.getValue(), field
										.getName(), values);
								tempFieldName = field.getName();
								sampleVString = fieldTypeDTO.getSampleValue();
								fieldTypeDescription = fieldTypeDTO.getDescription();
								break;
							} else {
								tempVWidget = GWTValidatableControl.createGWTControl(field.getType(), field.getValue(), field
										.getName(), alternateValuesSet, fieldTypeDTO.getRegexPatternList(), fieldTypeDTO
										.getSampleValue());
								tempFieldName = field.getName();
								sampleVString = fieldTypeDTO.getSampleValue();
								fieldTypeDescription = fieldTypeDTO.getDescription();
								break;
							}
						}
					} else {
						isFieldHidden = true;
					}
				}
				final String fieldNameString = tempFieldName;
				final String sampleValueString = sampleVString;
				if (isShowListBox) {
					if (!isFieldHidden) {
						if (tempListBox == null) {
							tempListBox = GWTListBoxControl.createGWTListControl(field.getType(), field.getValue(), values);
						}
						tempListBox.addStyleName(ReviewValidateConstants.DROPBOX_STYLE);
						final ListBox vWidget = tempListBox;
						vWidget.setTitle(field.getName());
						final String isActualValueFound = vWidget.getElement().getAttribute("isActualValueFound");

						// decision to removeOverlay in case value not found in drop down
						// final boolean isRemoveOverlay = !Boolean.parseBoolean(isActualValueFound != null ? isActualValueFound : "");

						vWidget.insertItem(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.NONE_SELECTED_TEXT), 0);
						if (!Boolean.parseBoolean(isActualValueFound != null ? isActualValueFound : "")) {
							vWidget.setSelectedIndex(0);
						}

						validateListBoxSelection(vWidget);
						// vWidget.getWidget().addStyleName("validatePanelListBox");
						/*
						 * for (int k = 0; k < alternateValuesSet.size(); k++) { vWidget.getWidget().setTitle(field.getName()); }
						 */

						vWidget.addFocusHandler(new FocusHandler() {

							@Override
							public void onFocus(FocusEvent event) {
								setCurrentDocFieldWidget(field.getName());
								ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(field, false));

								// commented out this line to remove the overlay in case the actual value not found in drop down
								ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(true, null, null));
							}
						});

						vWidget.addChangeHandler(new ChangeHandler() {

							@Override
							public void onChange(ChangeEvent arg0) {
								validateListBoxSelection(vWidget);
								updateDocument(null, field.getName());
							}
						});

						if (fieldTypeDescription != null && !fieldTypeDescription.isEmpty()) {
							validationTable.setWidget(index++, 0, new Label(fieldTypeDescription));
						} else {
							validationTable.setWidget(index++, 0, new Label(field.getName()));
						}
						validationTable.setWidget(index++, 0, vWidget);
						addDocFieldWidget(field, vWidget);
					}
				} else {
					if (!isFieldHidden) {
						if (tempVWidget == null) {
							tempVWidget = GWTValidatableControl
									.createGWTControl(field.getType(), field.getValue(), alternateValuesSet);
						}
						final ValidatableWidget<SuggestBox> vWidget = tempVWidget;
						vWidget.getWidget().addStyleName("validatePanelListBox");
						for (int k = 0; k < alternateValuesSet.size(); k++) {
							vWidget.getWidget().setTitle(field.getName());
						}

						vWidget.getWidget().getTextBox().addFocusHandler(new FocusHandler() {

							@Override
							public void onFocus(FocusEvent event) {
								setCurrentDocFieldWidget(field.getName());
								ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(field));
								ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(vWidget.validate(), sampleValueString,
										fieldNameString));
							}
						});

						vWidget.getWidget().addSelectionHandler(new SelectionHandler<Suggestion>() {

							@Override
							public void onSelection(SelectionEvent<Suggestion> arg0) {
								String inputString = arg0.getSelectedItem().getReplacementString();
								setSuggestBoxEvents(field, inputString, vWidget);
							}
						});
						if (fieldTypeDescription != null && !fieldTypeDescription.isEmpty()) {
							validationTable.setWidget(index++, 0, new Label(fieldTypeDescription));
						} else {
							validationTable.setWidget(index++, 0, new Label(field.getName()));
						}
						validationTable.setWidget(index++, 0, vWidget.getWidget());
						addDocFieldWidget(field, vWidget);
					}
				}
			}
		}
	}

	public void updateDocument(CoordinatesList coordinatesList, String changedWidgetName) {
		presenter.document.setErrorMessage("");
		Boolean isValidDoc = Boolean.TRUE;
		for (DocFieldWidget docFieldWidget : docFieldWidgets) {
			if (docFieldWidget.widget != null) {
				docFieldWidget.field.setValue(docFieldWidget.widget.getWidget().getValue());
				if (!docFieldWidget.widget.validate()) {
					isValidDoc = Boolean.FALSE;
				}
			} else {
				// Check for the drop down if they have none selected
				if (docFieldWidget.lWidget != null) {
					if (docFieldWidget.lWidget.getSelectedIndex() == 0) {
						isValidDoc = Boolean.FALSE;
						docFieldWidget.field.setValue("");
					} else {
						docFieldWidget.field.setValue(docFieldWidget.lWidget.getItemText(docFieldWidget.lWidget.getSelectedIndex()));
					}
				}
			}
		}
		if (coordinatesList != null) {
			for (DocFieldWidget docFieldWidget : docFieldWidgets) {
				if (docFieldWidget.field.getName().equals(changedWidgetName)) {
					docFieldWidget.field.setCoordinatesList(coordinatesList);
				}
			}
		}
		if (isValidDoc) {
			isValidDoc = checkForInvalidDataTables(presenter.document);
		}
		presenter.document.setValid(isValidDoc);
		List<Document> documents = presenter.batchDTO.getBatch().getDocuments().getDocument();
		int docIndex = 0;
		for (Document doc : documents) {
			if (doc.getIdentifier().equals(presenter.document.getIdentifier())) {
				documents.set(docIndex, presenter.document);
				break;
			}
			docIndex++;
		}
	}

	private boolean checkForInvalidDataTables(Document document) {
		boolean isValidDoc = true;
		if (document.getDataTables() != null) {
			outerloop: for (DataTable dataTable : document.getDataTables().getDataTable()) {
				for (Row row : dataTable.getRows().getRow()) {
					for (Column column : row.getColumns().getColumn()) {
						if (!column.isValid()) {
							isValidDoc = false;
							document.setErrorMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.INVALID_TABLE,
									dataTable.getName()));
							break outerloop;
						}
					}
				}
			}
		}
		return isValidDoc;
	}

	@Override
	public void initializeWidget() {

	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		eventBus.addHandler(DocExpandEvent.TYPE, new DocExpandEventHandler() {

			@Override
			public void onExpand(DocExpandEvent event) {
				refreshPanel(event.getDocument());
			}
		});

		eventBus.addHandler(DocTypeChangeEvent.TYPE, new DocTypeChangeEventHandler() {

			@Override
			public void onDocumentTypeChange(DocTypeChangeEvent event) {
				refreshPanel(event.getDocumentType());
			}
		});

		eventBus.addHandler(RVKeyDownEvent.TYPE, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {
				if (presenter.batchDTO.getBatch().getBatchStatus().equals(BatchStatus.READY_FOR_VALIDATION)
						&& event.getEvent().isControlKeyDown()) {
					updateDocument(null, null);
				}
			}
		});

		eventBus.addHandler(RVKeyUpEvent.TYPE, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(RVKeyUpEvent event) {

				DocFieldWidget docFieldWidget = null;
				updateDocument(null, null);

				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeKeyCode()) {
						// Tab
						case '\t':
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								if (event.getEvent().isShiftKeyDown()) {
									break;
								}
								showErrorField = Boolean.FALSE;
								docFieldWidget = getNextDocFieldWidget(showErrorField);
								if (docFieldWidget != null) {
									setCurrentFieldWidgetFocus(docFieldWidget);
								} else {
									openNextDocument();
								}
							}
							break;
						// CTRL + .
						case 190:
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								showErrorField = Boolean.TRUE;
								isPrevious = Boolean.FALSE;
								docFieldWidget = getNextDocFieldWidget(showErrorField);
								if (docFieldWidget != null) {
									if (docFieldWidget.widget != null) {
										docFieldWidget.widget.getWidget().setFocus(true);
									} else {
										docFieldWidget.lWidget.setFocus(true);
									}
								} else {
									openNextDocument();
								}
							}
							break;
						// CTRL + ,
						case 188:
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								showErrorField = Boolean.TRUE;
								isPrevious = Boolean.TRUE;
								docFieldWidget = getPreviousDocFieldWidget(showErrorField);
								if (docFieldWidget != null) {
									if (docFieldWidget.widget != null) {
										docFieldWidget.widget.getWidget().setFocus(true);
									} else {
										docFieldWidget.lWidget.setFocus(true);
									}
								} else {
									openPreviousDocument();
								}
							}
							break;
						// CTRL + z
						case 90:
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								fuzzySearchText.setFocus(true);
							}
							break;
						case '5':
						case 101:
							presenter.setTableView(Boolean.TRUE);
							event.getEvent().getNativeEvent().preventDefault();
							if (showTableViewBtn.isVisible()) {
								presenter.onTableViewButtonClicked();
							}
							break;
						case '6':
						case 102:
							event.getEvent().getNativeEvent().preventDefault();
							presenter.setTableView(Boolean.FALSE);
							if (showTableViewBtn.isVisible()) {
								presenter.onTableViewBackButtonClicked();
							}
							break;
						default:
							break;
					}
				}

			}
		});

		eventBus.addHandler(AnimationCompleteEvent.TYPE, new AnimationCompleteEventHandler() {

			@Override
			public void onAnimationComplete(AnimationCompleteEvent event) {
				if (event.getIndex() == 1) {
					((Button) ((HorizontalPanel) ((DockLayoutPanel) event.getWidget()).getWidget(0)).getWidget(0)).setFocus(true);
				} else {
					presenter.setFocus();
				}

			}
		});

		eventBus.addHandler(TreeRefreshEvent.TYPE, new TreeRefreshEventHandler() {

			@Override
			public void refresh(TreeRefreshEvent treeRefreshEvent) {
				// If any of the following entities is null... this means there is no page or document left in the batch.
				// Set the validate panel visibility to false.
				Batch batch = treeRefreshEvent.getBatchDTO().getBatch(); 
				if (batch == null || batch.getDocuments() == null || batch.getDocuments().getDocument() == null
						|| batch.getDocuments().getDocument().size() < 1) {
					setVisible(Boolean.FALSE);
					return;
				}
			}
		});

		// This event will handle the fuzzy search event.
		eventBus.addHandler(FuzzySearchEvent.TYPE, new FuzzySearchEventHandler() {

			@Override
			public void onExpand(FuzzySearchEvent event) {
				int index = event.getIndex();
				boolean isError = false;
				if (null == fuzzyDataCarrier || fuzzyDataCarrier.isEmpty() || index >= fuzzyDataCarrier.size() || index == 0) {
					isError = true;
				} else {
					List<String> resultHeaderList = fuzzyDataCarrier.get(0);
					List<String> resultDataList = fuzzyDataCarrier.get(index);
					if (null == resultDataList || resultDataList.isEmpty() || null == resultHeaderList || resultHeaderList.isEmpty()) {
						isError = true;
					} else {
						int jIndex = 0;
						for (String headerName : resultHeaderList) {
							String data = resultDataList.get(jIndex);
							setFuzzySerachValue(headerName, data);
							jIndex++;
						}
					}
				}
				if (isError) {
					// Window.alert("Unsuccessful fuzzy search");
					final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
					confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.msg_fuzzy_search_unsuccessful));
					confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
							ReviewValidateConstants.fuzzy_search_tooltip));
					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							confirmationDialog.hide();
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onCancelClick() {
							ScreenMaskUtility.unmaskScreen();
							presenter.setFocus();
						}
					});
					confirmationDialog.center();
					confirmationDialog.show();
					confirmationDialog.okButton.setFocus(true);
				}
			}
		});

		eventBus.addHandler(TableViewDisplayEvent.TYPE, new TableViewDisplayEventHandler() {

			@Override
			public void onResult(TableViewDisplayEvent event) {
				showTableViewBtn.setVisible(false);
				if (event.getTablesCount() > 0) {
					showTableViewBtn.setVisible(true);
				}
			}
		});

	}

	private static class DocFieldWidget {

		Field field;
		ValidatableWidget<SuggestBox> widget;
		ListBox lWidget;
		boolean isCurrent = false;

		public DocFieldWidget(Field field, ValidatableWidget<SuggestBox> widget) {
			this.field = field;
			this.widget = widget;
		}

		public DocFieldWidget(Field field, ListBox lWidget) {
			this.field = field;
			this.lWidget = lWidget;
		}

		@Override
		public int hashCode() {
			return field.getName().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return this.field.getName().equals(((DocFieldWidget) obj).field.getName());
		}

		public void setCurrent(boolean isCurrent) {
			this.isCurrent = isCurrent;
		}
	}

	private final LinkedList<DocFieldWidget> docFieldWidgets = new LinkedList<DocFieldWidget>();

	private void addDocFieldWidget(Field field, ValidatableWidget<SuggestBox> widget) {
		docFieldWidgets.add(new DocFieldWidget(field, widget));
	}

	private void addDocFieldWidget(Field field, ListBox lWidget) {
		docFieldWidgets.add(new DocFieldWidget(field, lWidget));
	}

	private DocFieldWidget getNextDocFieldWidget(boolean isError) {
		Boolean reachedToCurrent = Boolean.FALSE;
		DocFieldWidget nextDocFieldWidget = null;
		for (DocFieldWidget docFieldWidget : docFieldWidgets) {
			if (docFieldWidget.widget != null) {
				if (docFieldWidget.isCurrent) {
					docFieldWidget.widget.getWidget().hideSuggestionList();
					reachedToCurrent = Boolean.TRUE;
					continue;
				}

				if (reachedToCurrent && (!isError || !docFieldWidget.widget.validate())) {
					nextDocFieldWidget = docFieldWidget;
					break;
				}
			} else {
				if (docFieldWidget.isCurrent) {
					reachedToCurrent = Boolean.TRUE;
					continue;
				}
				if (reachedToCurrent && (!isError || !validateListBoxSelection(docFieldWidget.lWidget))) {
					nextDocFieldWidget = docFieldWidget;
					break;
				}
			}
		}
		return nextDocFieldWidget;
	}

	public void setSelectedValues(String value, CoordinatesList coordinatesList, String pageid) {
		boolean isAdded = false;
		DocFieldWidget docFieldWidget = getCurrentDocFieldWidget();
		if (docFieldWidget.widget != null) {
			// docFieldWidget.widget.getWidget().setText(value);
			docFieldWidget.widget.getWidget().setValue(value);
			ValueChangeEvent.fire(docFieldWidget.widget.getWidget(), value);
			docFieldWidget.field.setCoordinatesList(coordinatesList);
			docFieldWidget.field.setPage(pageid);
			docFieldWidget.widget.getWidget().setFocus(true);
		} else {
			for (int i = 0; i < docFieldWidget.lWidget.getItemCount(); i++) {
				if (docFieldWidget.lWidget.getItemText(i).equalsIgnoreCase(value)) {
					docFieldWidget.lWidget.setSelectedIndex(i);
					isAdded = true;
				}
			}
			if (!isAdded) {
				docFieldWidget.lWidget.addItem(value);
				docFieldWidget.lWidget.setSelectedIndex(docFieldWidget.lWidget.getItemCount() - 1);
			}
		}
	}

	private DocFieldWidget getPreviousDocFieldWidget(boolean isError) {
		Boolean reachedToCurrent = Boolean.FALSE;
		DocFieldWidget previousDocFieldWidget = null;
		for (DocFieldWidget docFieldWidget : new ReverseIterable<DocFieldWidget>(docFieldWidgets)) {
			if (docFieldWidget.widget != null) {
				if (docFieldWidget.isCurrent) {
					docFieldWidget.widget.getWidget().hideSuggestionList();
					reachedToCurrent = Boolean.TRUE;
					continue;
				}

				if (reachedToCurrent && (!isError || !docFieldWidget.widget.validate())) {
					previousDocFieldWidget = docFieldWidget;
					break;
				}
			} else {
				if (docFieldWidget.isCurrent) {
					reachedToCurrent = Boolean.TRUE;
					continue;
				}
				if (reachedToCurrent && (!isError || !validateListBoxSelection(docFieldWidget.lWidget))) {
					previousDocFieldWidget = docFieldWidget;
					break;
				}
			}
		}
		return previousDocFieldWidget;
	}

	private DocFieldWidget getCurrentDocFieldWidget() {
		DocFieldWidget currentDocFieldWidget = null;
		for (DocFieldWidget docFieldWidget : new ReverseIterable<DocFieldWidget>(docFieldWidgets)) {
			if (docFieldWidget.widget != null) {
				if (docFieldWidget.isCurrent) {
					docFieldWidget.widget.getWidget().hideSuggestionList();
					currentDocFieldWidget = docFieldWidget;
					break;
				}

			} else {
				if (docFieldWidget.isCurrent) {
					currentDocFieldWidget = docFieldWidget;
					break;
				}
			}
		}
		return currentDocFieldWidget;
	}

	// private void setCurrentDocFieldWidget(DocFieldWidget dfWidget) {
	// setCurrentDocFieldWidget(dfWidget.field.getName());
	// }

	private void setCurrentDocFieldWidget(String fieldName) {
		for (DocFieldWidget docFieldWidget : docFieldWidgets) {
			docFieldWidget.setCurrent(false);

			if (docFieldWidget.field.getName().equals(fieldName)) {
				docFieldWidget.setCurrent(true);
				// docFieldWidget.widget.getWidget().setFocus(true);
			}
		}
	}

	private void setCurrentFieldWidgetFocus(DocFieldWidget dfWidget) {
		dfWidget.setCurrent(true);
		if (dfWidget.widget != null) {
			dfWidget.widget.getWidget().setFocus(true);
		} else {
			dfWidget.lWidget.setFocus(true);
		}
	}

	/**
	 * This method is used to set value of field name in the existing document level fields. This will set the value to the widget
	 * field value and validate it.
	 * 
	 * @param fieldName String
	 * @param fieldValue String
	 */
	private void setFuzzySerachValue(String fieldName, String fieldValue) {
		for (DocFieldWidget docFieldWidget : docFieldWidgets) {
			if (docFieldWidget.field.getName().equals(fieldName)) {
				docFieldWidget.field.setValue(fieldValue);
				docFieldWidget.widget.getWidget().setValue(fieldValue);
				docFieldWidget.widget.toggleValidDateBox();
			}
		}
	}

	private void setSuggestBoxEvents(DocField field, String inputString, ValidatableWidget<SuggestBox> vWidget) {

		int pos = inputString.lastIndexOf(SEPERATOR);
		int index = 0;
		String originalString = inputString;
		if (!(pos < 0)) {
			index = Integer.parseInt(inputString.substring(pos + ALTERNATE_STRING_VALUE.length() + SEPERATOR.length(), inputString
					.length()));
			inputString = inputString.substring(0, pos);
		}
		vWidget.getWidget().setValue(inputString);
		vWidget.toggleValidDateBox();
		CoordinatesList coordinatesList = field.getCoordinatesList();
		int count = 0;
		if (field.getAlternateValues() != null) {
			for (Field alternateField : field.getAlternateValues().getAlternateValue()) {
				if (pos < 0) {
					if (alternateField.getValue().equals(inputString)) {
						ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(alternateField));
						coordinatesList = alternateField.getCoordinatesList();
					}
				} else {
					if (alternateField.getValue().equals(inputString)) {
						if (count == index) {
							ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(alternateField));
							coordinatesList = alternateField.getCoordinatesList();
						}
						count++;
					}
				}

			}
		}
		if (field.getValue().equals(originalString)) {
			ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(field));
		}
		updateDocument(coordinatesList, field.getName());
	}

	public Button getShowTableViewBtn() {
		return showTableViewBtn;
	}

	private boolean validateListBoxSelection(ListBox listbox) {
		boolean isValid = true;
		if (listbox.getSelectedIndex() == 0) {
			listbox.addStyleName("dateBoxFormatError");
			isValid = false;
		} else {
			listbox.removeStyleName("dateBoxFormatError");
			isValid = true;
		}
		return isValid;
	}

	public void openNextDocument() {
		Document previousDoc = presenter.document;
		presenter.document = presenter.batchDTO.getNextDocumentTo(presenter.document, showErrorField);
		if (enableSwitchScriptValidation.equalsIgnoreCase(presenter.batchDTO.getIsScriptEnabled())) {
			executeScript(previousDoc);

		} else {
			ValidatePanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, null));
		}
	}

	private void openPreviousDocument() {
		Document previousDoc = presenter.document;
		presenter.document = presenter.batchDTO.getPreviousDocumentTo(presenter.document, showErrorField);
		if (presenter.document == null) {
			presenter.document = previousDoc;
		}
		if (enableSwitchScriptValidation.equalsIgnoreCase(presenter.batchDTO.getIsScriptEnabled())) {
			executeScript(previousDoc);
		} else {
			ValidatePanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, null));
		}
	}

	public void executeScript(final Document previousDocument) {
		final Batch batch = presenter.batchDTO.getBatch();
		ScreenMaskUtility.maskScreen("Executing Script....");

		presenter.rpcService.executeScript(batch, new AsyncCallback<BatchDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				String title = LocaleDictionary.get().getConstantValue(ReviewValidateConstants.SCRIPT_EXECUTION);
				String message = LocaleDictionary.get().getMessageValue(ReviewValidateMessages.msg_script_execution_failed);
				ScreenMaskUtility.unmaskScreen();
				showConfirmationDialog(title, message);
			}

			@Override
			public void onSuccess(BatchDTO batchDTO) {
				presenter.batchDTO = batchDTO;
				Document doc = null;
				boolean showPopUp = false;
				List<Document> documents = batchDTO.getBatch().getDocuments().getDocument();
				Document selectedDocument = null;
				for (Document document : documents) {
					if (document.getIdentifier().equalsIgnoreCase(previousDocument.getIdentifier())) {
						if (!document.isValid()) {
							doc = document;
							selectedDocument = document;
							setFieldValues(previousDocument, selectedDocument);
							showPopUp = true;
						} else {
							selectedDocument = presenter.document;
							ValidatePanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, null));
						}
						break;
					}
				}
				if (selectedDocument != null) {
					ValidatePanel.this.fireEvent(new DocumentRefreshEvent(selectedDocument));
				}
				ScreenMaskUtility.unmaskScreen();

				if (showPopUp) {
					ValidatePanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, doc, null));
				}

			}
		});
	}

	private void showConfirmationDialog(String title, String message) {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
		confirmationDialog.setMessage(message);
		confirmationDialog.setDialogTitle(title);
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onCancelClick() {
				ScreenMaskUtility.unmaskScreen();
				presenter.setFocus();
			}
		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	private void setFieldValues(Document previousDocument, Document selectedDocument) {
		selectedDocument.setDocumentLevelFields(previousDocument.getDocumentLevelFields());
	}
}
