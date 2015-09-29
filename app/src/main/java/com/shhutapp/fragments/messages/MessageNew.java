package com.shhutapp.fragments.messages;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView ivLocation;
    private boolean isError;
    private int x = -1;
    private int y = -1;
    private int offset = -1;
    private final Bitmap bitmap = BitmapFactory.decodeResource(getMainActivity().getResources(), R.drawable.geo_on);
    public MessageNew(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public MessageNew(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
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
                    if(page.getMessageListFragment() != null) page.getMessageListFragment().onUpdateData();
                    edMessage.setText("");
                }
                getMainActivity().getHeader().setInvisibleAll();
                getMainActivity().getHeader().setVisibleBack(true);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(getIAm()).
                        add(R.id.rlList, page.getMessageListFragment()).
                        //add(R.id.messagePage, page.getEmptyFragment()).
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
        ivLocation = (ImageView) rView.findViewById(R.id.ivMessageNewLocation);
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable back = edMessage.getBackground();
                if (back == null) {
                    back = new Drawable() {
                        @Override
                        public void draw(Canvas canvas) {
                            Bitmap b = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, edMessage.getHeight(), false);
                            Paint pText = new Paint();
                            pText.setTextSize(edMessage.getTextSize());
                            pText.setTypeface(edMessage.getTypeface());
                            Rect r = new Rect();
                            pText.getTextBounds(edMessage.getText().toString(), 0, edMessage.getText().toString().length(), r);
                            if (offset < 0) {
                                offset = r.width();
                                String s = edMessage.getText().toString() + "      ";
                                edMessage.setText(s);
                                edMessage.setSelection(s.length(), s.length());
                                ivLocation.setEnabled(false);
                            }
                            Matrix m = new Matrix();
                            m.postTranslate(offset, 0);
                            Paint p = new Paint();
                            canvas.drawBitmap(b, m, p);
                        }

                        @Override
                        public void setAlpha(int alpha) {
                        }

                        @Override
                        public void setColorFilter(ColorFilter cf) {
                        }

                        @Override
                        public int getOpacity() {
                            return 0;
                        }
                    };
                    edMessage.setBackground(back);
                }
            }
        });
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
        final String s = card.getText();
        final int beg = s.indexOf("{Loc}");
        if(beg>0) {
            String res = s.substring(0, beg) + "      " + s.substring(beg + 5, s.length() - 1);
            if (card.getText() != null) edMessage.setText(res);
            Drawable dr = new Drawable() {
                @Override
                public void draw(Canvas canvas) {
                    Bitmap bit = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, edMessage.getHeight(), false);

                    Paint pText = new Paint();
                    pText.setTextSize(edMessage.getTextSize());
                    pText.setTypeface(edMessage.getTypeface());
                    Rect r = new Rect();
                    pText.getTextBounds(s.substring(0, beg+1), 0, s.substring(0, beg).length(), r);

                    Matrix m = new Matrix();
                    m.postTranslate(r.width(), 0);
                    Paint p = new Paint();
                    canvas.drawBitmap(bit, m, p);
                }

                @Override
                public void setAlpha(int alpha) {
                }

                @Override
                public void setColorFilter(ColorFilter cf) {
                }

                @Override
                public int getOpacity() {
                    return 0;
                }
            };
            ivLocation.setEnabled(false);
            edMessage.setBackground(dr);
        }
        else{
            edMessage.setText(card.getText());
        }
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
                if (edMessage.getText().toString().length() > 145) {
                    edMessage.setText(edMessage.getText().toString().substring(0, 144));
                    edMessage.setSelection(edMessage.getText().toString().length(), edMessage.getText().toString().length());
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
