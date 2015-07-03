package com.shhutapp.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.area.AreaCard;
import com.shhutapp.fragments.messages.MessageEmpty;
import com.shhutapp.fragments.Header;
import com.shhutapp.fragments.messages.MessageList;
import com.shhutapp.fragments.messages.MessageListListener;
import com.shhutapp.fragments.messages.MessageNew;
import com.shhutapp.fragments.messages.MessageScale;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnCancelListener;
import com.shhutapp.geo.Area;

public class MessagePage extends BasePage {

    private Header header;
    private MessageScale scale;
    private MessageList messagesList;
    private MessageEmpty empty;
    private MessageNew newMessage;
    private BasePage prev;
    private static MessagePage instance;

    public MessagePage(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public  MessagePage(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public  MessagePage(MainActivity act, BasePage prev){
        super(act);
        this.prev = prev;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prevID = getArguments().getInt("back");
        header = getMainActivity().getHeader();
        scale = new MessageScale(getMainActivity(), this);
        messagesList = new MessageList(getMainActivity(), this);
        empty = new MessageEmpty(getMainActivity(), this);
        newMessage = new MessageNew(getMainActivity(), this);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.message_page, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        header.setHeight(56);
        header.setInvisibleAll();
        header.setLeftText(72);
        header.setTextHeader(getMainActivity().getResources().getString(R.string.messages));
        header.setVisibleBack(true);
        header.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                header.setInvisibleAll();
                header.setVisibleBack(true);
                if (act.isMessageListEmpty()) {
                    fragmentManager().beginTransaction().
                            remove(newMessage).
                            add(R.id.messagePage, scale).commit();
                    fragmentManager().beginTransaction().add(R.id.messagePage, messagesList).
                            commit();
                } else {
                    fragmentManager().beginTransaction().
                            remove(newMessage).
                            add(R.id.messagePage, messagesList).commit();
                    fragmentManager().beginTransaction().add(R.id.messagePage, empty).
                            commit();
                }
            }
        });
        header.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                switch (prevID) {
                    case BasePage.Pages.mainPage: {
                        BasePage page = getMainActivity().createPageFromID(prevID);
                        getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                        getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, page).commit();
                        break;
                    }
                    case BasePage.Pages.queitTimePage:{
                        getMainActivity().getHeader().setVisibleBack(false);
                        getMainActivity().getHeader().setVisibleCancel(true);

                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                remove(getCurrent()).
                                show(QueitTimePage.instance).
                                commit();
                        break;
                    }
                    case Pages.areaCard:{
                        getMainActivity().getHeader().setVisibleBack(false);
                        getMainActivity().getHeader().setVisibleCancel(true);

                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                remove(getCurrent()).
                                show(AreaCard.instance).
                                commit();
                        break;
                    }

                }
            }
        });
        scale.setHeight(getMainActivity().isMessageListEmpty() ? 98 : 0);
        messagesList.setHeight(getMainActivity().isMessageListEmpty() ? 486 : 640 - 56 - 48);
        messagesList.showEmpty(getMainActivity().isMessageListEmpty());
        messagesList.setOnMessageListListener(new MessageListListener() {
            @Override
            public void onAdd() {
                header.setInvisibleAll();
                header.setVisibleCancel(true);
                Bundle b = new Bundle();
                b.putBoolean("isArgs", false);
                newMessage.setArguments(b);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        addToBackStack(null).
                        remove(empty).
                        remove(messagesList).
                        add(R.id.messagePage, newMessage).
                        commit();
            }
            @Override
            public void onDelete() {
            }
            @Override
            public void onEdit(int id) {
                header.setInvisibleAll();
                header.setVisibleCancel(true);
                //newMessage = new MessageNew(getMainActivity(), instance);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        addToBackStack(null).
                        remove(empty).
                        remove(messagesList).
                        add(R.id.messagePage, newMessage).
                        commit();
            }
            @Override
            public void onSelected(int id) {
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        if(act.isMessageListEmpty()) {
            fragmentManager().beginTransaction().
                    //addToBackStack(null).
                    add(R.id.messagePage, scale).
                    add(R.id.messagePage, messagesList).
                    commit();
        }
        else{
            Log.i("Resume", "Resumed");
            fragmentManager().beginTransaction().
                    //addToBackStack(null).
                    add(R.id.messagePage, messagesList).
                    add(R.id.messagePage, empty).
                    commit();
        }
    }
    @Override
    public int getID(){
        return Pages.messagePage;
    }
    public MessageEmpty getEmptyFragment(){
        return empty;
    }
    public MessageList getMessageListFragment(){
        return messagesList;
    }
    public MessageNew getMessageNewFragment(){
        return  newMessage;
    }
}
