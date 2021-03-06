/*
 *  Copyright 2019 Arcus Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package arcus.app.common.popups;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import arcus.app.R;
import arcus.app.common.backstack.BackstackManager;
import arcus.app.common.view.ScleraButton;
import arcus.app.common.view.ScleraButtonColor;
import arcus.app.common.view.ScleraTextView;


public class ScleraInfoButtonPopup extends ArcusFloatingFragment {
    private final static String TITLE = "TITLE";
    private final static String SUB_TITLE = "SUB_TITLE";
    private final static String MESSAGE = "MESSAGE";
    private final static String TOP_BUTTON_TEXT = "TOP_BUTTON_TEXT";
    private final static String BOTTOM_BUTTON_TEXT = "BOTTOM_BUTTON_TEXT";
    private final static String TOP_BUTTON_COLOR = "TOP_BUTTON_COLOR";
    private final static String BOTTOM_BUTTON_COLOR = "BOTTOM_BUTTON_COLOR";
    private final static String SHOW_CLOSE = "SHOW_CLOSE";
    private Callback callbackRef;

    public interface Callback {
        void confirmationValue(boolean confirm);
    }

    public static ScleraInfoButtonPopup newInstance(
          @NonNull String title,
          @NonNull String message,
          @NonNull String topButtonText,
          @NonNull String bottomButtonText
    ) {
        return newInstance(title, message, topButtonText, bottomButtonText, null, null);
    }

    public static ScleraInfoButtonPopup newInstance(
            @NonNull String title,
            @NonNull String message,
            @NonNull String topButtonText,
            @NonNull String bottomButtonText,
            @NonNull boolean showClose
    ) {
        return newInstance(title, null, message, topButtonText, bottomButtonText, null, null, showClose);
    }

    public static ScleraInfoButtonPopup newInstance(
          @NonNull String title,
          @NonNull String message,
          @NonNull String topButtonText,
          @NonNull String bottomButtonText,
          @Nullable ScleraButtonColor topButtonColorScheme,
          @Nullable ScleraButtonColor bottomButtonColorScheme
    ) {
        return newInstance(title, null, message, topButtonText, bottomButtonText, topButtonColorScheme, bottomButtonColorScheme);
    }

    public static ScleraInfoButtonPopup newInstance(
          @NonNull String title,
          @NonNull String message,
          @NonNull String topButtonText,
          @NonNull String bottomButtonText,
          @Nullable ScleraButtonColor topButtonColorScheme,
          @Nullable ScleraButtonColor bottomButtonColorScheme,
          @NonNull boolean showClose
    ) {
        return newInstance(title, null, message, topButtonText, bottomButtonText, topButtonColorScheme, bottomButtonColorScheme, showClose);
    }

    public static ScleraInfoButtonPopup newInstance(
          @NonNull String title,
          @Nullable String subtitle,
          @NonNull String message,
          @NonNull String topButtonText,
          @NonNull String bottomButtonText,
          @Nullable ScleraButtonColor topButtonColorScheme,
          @Nullable ScleraButtonColor bottomButtonColorScheme
    ) {
        return newInstance(title, subtitle, message, topButtonText, bottomButtonText, topButtonColorScheme, bottomButtonColorScheme, true);
    }

    public static ScleraInfoButtonPopup newInstance(
            @NonNull String title,
            @Nullable String subtitle,
            @NonNull String message,
            @NonNull String topButtonText,
            @NonNull String bottomButtonText,
            @Nullable ScleraButtonColor topButtonColorScheme,
            @Nullable ScleraButtonColor bottomButtonColorScheme,
            @NonNull boolean showClose
    ) {

        ScleraInfoButtonPopup fragment = new ScleraInfoButtonPopup();
        Bundle args = new Bundle(7);

        args.putString(TITLE, title);
        args.putString(SUB_TITLE, subtitle);
        args.putString(MESSAGE, message);
        args.putString(TOP_BUTTON_TEXT, topButtonText);
        args.putString(BOTTOM_BUTTON_TEXT, bottomButtonText);
        args.putInt(TOP_BUTTON_COLOR, topButtonColorScheme == null ? 0 : topButtonColorScheme.ordinal());
        args.putInt(BOTTOM_BUTTON_COLOR, bottomButtonColorScheme == null ? 0 : bottomButtonColorScheme.ordinal());
        args.putBoolean(SHOW_CLOSE, showClose);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setFloatingTitle() {
        title.setText(getTitle());
    }

    @Override
    public void doContentSection() {
        if (super.title != null) {
            super.title.setVisibility(View.GONE);
        }

        ScleraTextView infoTitleView = (ScleraTextView) contentView.findViewById(R.id.info_text_title);
        ScleraTextView infoSubTitleView = (ScleraTextView) contentView.findViewById(R.id.info_text_sub_title);
        ScleraTextView infoMessageView = (ScleraTextView) contentView.findViewById(R.id.info_text_message);
        ScleraButton correct = (ScleraButton) contentView.findViewById(R.id.btn_correct);
        ScleraButton edit = (ScleraButton) contentView.findViewById(R.id.btn_edit);

        String infoTitle = getArguments().getString(TITLE);
        String infoSubTitle = getArguments().getString(SUB_TITLE);
        String infoMessage = getArguments().getString(MESSAGE);
        String topButtonText = getArguments().getString(TOP_BUTTON_TEXT);
        String bottomButtonText = getArguments().getString(BOTTOM_BUTTON_TEXT);
        int topButtonColor = getArguments().getInt(TOP_BUTTON_COLOR, 0);
        int bottomButtonColor = getArguments().getInt(BOTTOM_BUTTON_COLOR, 0);
        boolean showClose = getArguments().getBoolean(SHOW_CLOSE, false);
        setHasCloseButton(showClose);


        infoTitleView.setText(infoTitle);
        if (!TextUtils.isEmpty(infoSubTitle)) {
            infoSubTitleView.setText(infoSubTitle);
            infoSubTitleView.setVisibility(View.VISIBLE);
        }
        infoMessageView.setText(infoMessage);
        correct.setText(topButtonText);
        correct.setColorScheme(ScleraButtonColor.values()[topButtonColor]);
        edit.setText(bottomButtonText);
        edit.setColorScheme(ScleraButtonColor.values()[bottomButtonColor]);

        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callbackRef != null) {
                    doClose();
                    BackstackManager.getInstance().navigateBack();
                    callbackRef.confirmationValue(true);
                    callbackRef = null;
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callbackRef != null) {
                    doClose();
                    BackstackManager.getInstance().navigateBack();
                    callbackRef.confirmationValue(false);
                    callbackRef = null;
                }
            }
        });
    }

    @Override
    public Integer contentSectionLayout() {
        return R.layout.floating_sclera_info_button_popup;
    }

    @Override
    public String getTitle() {
        return null;
    }

    public void setCallback(ScleraInfoButtonPopup.Callback callback) {
        callbackRef = callback;
    }
}
