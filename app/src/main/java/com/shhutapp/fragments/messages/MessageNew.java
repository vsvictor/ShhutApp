package com.shhutapp.fragments.messages;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.BaseObject;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.SMSCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnOkListener;
import com.shhutapp.pages.BasePage;
import com.shhutapp.pages.MessagePage;

/**
 * Created by victor on 04.05.15.
 */
public class MessageNew extends BaseFragments {
    private EditText edMessage;
    private MessagePage page;
    private SMSCard card;
    private RelativeLayout rlEditTextNormal;
    private RelativeLayout rlEditTextError;
    private boolean isError;
    public MessageNew(){
        super(MainActivity.getMainActivity());
    }
    public MessageNew(MainActivity act){
        super(act);
    }
    public MessageNew(MainActivity act, MessagePage page){
        super(act);
        this.page = page;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getMainActivity().getHeader().setOnOkListener(new OnOkListener() {
            @Override
            public void onOk() {
                hideKeyboard();
                String sms = edMessage.getText().toString();
                if (isPresent(sms)) {
                    setError(true);
                    return;
                }
                if (sms != null) {
                    card.setText(sms);
                    card.save(getMainActivity().getDB());
                    page.getMessageListFragment().onUpdateData();
                    edMessage.setText("");
                }
                getMainActivity().getHeader().setInvisibleAll();
                getMainActivity().getHeader().setVisibleBack(true);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(getIAm()).
                        add(R.id.messagePage, page.getMessageListFragment()).
                        add(R.id.messagePage, page.getEmptyFragment()).
                        commit();
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.new_message, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        edMessage = (EditText)rView.findViewById(R.id.edMessageNew);
        rlEditTextNormal = (RelativeLayout) rView.findViewById(R.id.rlEditTextNormal);
        rlEditTextError  = (RelativeLayout) rView.findViewById(R.id.rlEditTextError);
        setError(false);
    }
    @Override
    public void onResume(){
        super.onResume();
        Bundle b = getArguments();
        if(b != null && b.getBoolean("isArgs")){
            card = new SMSCard();
            card.setID(b.getInt("id"));
            card.setText(b.getString("text"));
        }
        else card = new SMSCard();
        if(card.getText() != null) edMessage.setText(card.getText());
        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(false);
                if (edMessage.getText().toString().length() > 3) {
                    getMainActivity().getHeader().setVisibleOk(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        showKeyboadr();
    }
    public void setEnteredText(String message){
        edMessage.setText(message);
    }
    public String getEnteredText(){
        return edMessage.getText().toString();
    }
    public boolean isPresent(String message){
        BaseObjectList list = getMainActivity().getDBHelper().loadMessages();
        for(BaseObject card : list){
            String mess = ((SMSCard) card).getText();
            if(mess.equalsIgnoreCase(message)) return true;
        }
        return false;
    }
    private void setError(boolean b){
        if(b){
            rlEditTextNormal.setVisibility(View.INVISIBLE);
            rlEditTextError.setVisibility(View.VISIBLE);
        }
        else{
            rlEditTextNormal.setVisibility(View.VISIBLE);
            rlEditTextError.setVisibility(View.INVISIBLE);
        }
    }
}
