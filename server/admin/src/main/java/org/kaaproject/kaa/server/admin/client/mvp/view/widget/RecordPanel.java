/*
 * Copyright 2014-2015 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.kaa.server.admin.client.mvp.view.widget;

import org.kaaproject.avro.ui.gwt.client.widget.RecordFieldWidget;
import org.kaaproject.avro.ui.shared.RecordField;
import org.kaaproject.kaa.server.admin.client.KaaAdmin;
import org.kaaproject.kaa.server.admin.client.util.HasErrorMessage;
import org.kaaproject.kaa.server.admin.client.util.Utils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class RecordPanel extends SimplePanel implements HasValue<RecordField>, ChangeHandler {

    private static final String REQUIRED = Utils.fieldWidgetStyle.requiredField();
    
    private RecordFieldWidget recordFieldWidget;
    private FileUploadForm recordFileUpload;
    private Button uploadButton;    
    private String recordFileItemName;
    private boolean readOnly;
    private HasErrorMessage hasErrorMessage;
    
    public RecordPanel(String title, HasErrorMessage hasErrorMessage, boolean optional, boolean readOnly) {
        this.readOnly = readOnly;
        this.hasErrorMessage = hasErrorMessage;
        FlexTable table = new FlexTable();
        CaptionPanel recordCaption = new CaptionPanel();
        if (optional) {
            recordCaption.setCaptionText(title);
        } else {
            SpanElement span = Document.get().createSpanElement();
            span.appendChild(Document.get().createTextNode(title));
            span.addClassName("gwt-Label");
            span.addClassName(REQUIRED);
            recordCaption.setCaptionHTML(span.getString());
        }        
        recordFieldWidget = new RecordFieldWidget();
        recordCaption.setContentWidget(recordFieldWidget);
        recordCaption.setWidth("100%");
        table.setWidget(0, 0, recordCaption);
        if (!readOnly) {
            Widget label = new Label(Utils.constants.uploadFromFile());
            recordFileUpload = new FileUploadForm();
            recordFileUpload.setWidth("200px");
            recordFileUpload.addSubmitCompleteHandler(new SubmitCompleteHandler() {
                @Override
                public void onSubmitComplete(SubmitCompleteEvent event) {
                    loadRecordFromFile();
                }
            });
            recordFileUpload.addChangeHandler(this);
            recordFileItemName = recordFileUpload.getFileItemName();
            uploadButton = new Button(Utils.constants.upload(), new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    recordFileUpload.submit();
                }
            });
            uploadButton.addStyleName(Utils.kaaAdminStyle.bAppButtonSmall());
            uploadButton.setEnabled(false);
            table.setWidget(1, 0, label);
            table.setWidget(1, 1, recordFileUpload);
            table.setWidget(1, 2, uploadButton);
            table.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);
            table.getFlexCellFormatter().setColSpan(0, 0, 3);
        }
        setWidget(table);
    }
    
    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<RecordField> handler) {
        return recordFieldWidget.addValueChangeHandler(handler);
    }

    @Override
    public RecordField getValue() {
        return recordFieldWidget.getValue();
    }

    @Override
    public void setValue(RecordField value) {
        recordFieldWidget.setValue(value);
    }

    @Override
    public void setValue(RecordField value, boolean fireEvents) {
        recordFieldWidget.setValue(value, fireEvents);
    }
    
    public void reset() {
        recordFieldWidget.setValue(null);
        if (!readOnly) {
            recordFileUpload.reset();
            uploadButton.setEnabled(false);
        }
    }
    
    public boolean validate() {
        return recordFieldWidget.validate();
    }
    
    private void loadRecordFromFile() {
        String schema = recordFieldWidget.getValue().getSchema();
        KaaAdmin.getDataSource().getRecordDataFromFile(schema, recordFileItemName, 
                new AsyncCallback<RecordField>() {
                    @Override
                    public void onSuccess(RecordField result) {
                        setValue(result, true);
                        recordFileUpload.reset();
                        uploadButton.setEnabled(false);
                    }
                    
                    @Override
                    public void onFailure(Throwable caught) {
                        Utils.handleException(caught, hasErrorMessage);
                    }
                });
    }

    @Override
    public void onChange(ChangeEvent event) {
        boolean enabled = recordFileUpload.getFileName().length()>0 && 
                          recordFieldWidget.getValue() != null;
        uploadButton.setEnabled(enabled);
    }

}