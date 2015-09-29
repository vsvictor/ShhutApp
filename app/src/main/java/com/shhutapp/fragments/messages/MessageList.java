package com.shhutapp.fragments.messages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.FilteredAdapter;
import com.shhutapp.data.SMSCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.Header;
import com.shhutapp.pages.MessagePage;

public class MessageList extends BaseFragments {
    private boolean isEmpty;
    private Header header;
    private MessagePage page;
    private RelativeLayout rlMessageListEmpty;
    private RelativeLayout rlMessageListData;
    private RelativeLayout rlMessageListBottom;
    private ImageView ivButton;
    private RelativeLayout rlAddButton;

    private SwipeListView swMessages;
    private BaseObjectList list;
    private MessageAdapter adapter;
    private MessageListListener listener;

    public MessageList(){
        super();
    }
    @SuppressLint("ValidFragment")
    public MessageList(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public MessageList(MainActivity act, MessagePage page){
        super(act);
        this.page = page;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        list = act.getDBHelper().loadMessages();
        adapter = new MessageAdapter(act, list);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.messages_list, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        rlMessageListEmpty = (RelativeLayout) rView.findViewById(R.id.rlMessageListEmpty);
        rlMessageListData  = (RelativeLayout) rView.findViewById(R.id.rlMessageListData);
        rlMessageListBottom = (RelativeLayout) rView.findViewById(R.id.rlWhiteListBottom);

        isEmpty = getMainActivity().isMessageListEmpty();
        rlMessageListEmpty.setVisibility(isEmpty?View.VISIBLE:View.INVISIBLE);
        rlMessageListData.setVisibility(!isEmpty ? View.VISIBLE : View.INVISIBLE);

        ivButton = (ImageView) rView.findViewById(R.id.ivMessageListAllMessage);
        ivButton .setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlMessageListEmpty.setLayoutParams(p);
        rlMessageListData.setLayoutParams(p);
        rlAddButton = (RelativeLayout) rView.findViewById(R.id.rlMessageListAddButton);
        rlAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAdd();
                adapter.notifyDataSetUpdated();
            }
        });
        swMessages = (SwipeListView) rView.findViewById(R.id.lvMessageList);
        swMessages.setAdapter(adapter);
        swMessages.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                View v = (View) swMessages.getChildAt(position - swMessages.getFirstVisiblePosition());
                RelativeLayout llBack = (RelativeLayout) v.findViewById(R.id.message_list_item_back);
                llBack.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
                View v = (View) swMessages.getChildAt(position - swMessages.getFirstVisiblePosition());
                RelativeLayout llBack = (RelativeLayout) v.findViewById(R.id.message_list_item_back);
                llBack.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                swMessages.closeOpenedItems();
            }

            @Override
            public void onStartClose(int position, boolean right) {
            }

            @Override
            public void onClickFrontView(int position) {
                int id = ((SMSCard) ((MessageAdapter) swMessages.getAdapter()).getData().get(position)).getID();
                String s = ((SMSCard) ((MessageAdapter) swMessages.getAdapter()).getData().get(position)).getText();
                Bundle b = new Bundle();
                b.putBoolean("isArgs", true);
                b.putInt("id", id);
                b.putString("text", s);
                page.getMessageNewFragment().setArguments(b);
                listener.onEdit(id);
                adapter.notifyDataSetUpdated();
            }
            @Override
            public void onClickBackView(int position) {
                int id = ((SMSCard) ((MessageAdapter) swMessages.getAdapter()).getData().get(position)).getID();
                act.getDBHelper().deleteMessage(id);
                adapter.notifyDataSetUpdated();
            }
            @Override
            public void onDismiss(int[] reverseSortedPositions) {
            }
        });
    }
    @Override
    public void setHeight(int dp){
        super.setHeight(dp);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if(rlMessageListEmpty != null) rlMessageListEmpty.setLayoutParams(p);
        if(rlMessageListData != null) rlMessageListData.setLayoutParams(p);
    }
    @Override
    public void onResume(){
        super.onResume();
        adapter.notifyDataSetUpdated();
        //page.getMessageNewFragment().setArguments(null);
    }
    public void showEmpty(boolean isMessageListEmpty){
        isEmpty = isMessageListEmpty;
    }
    public void setOnMessageListListener(MessageListListener aListener){
        listener = aListener;
    }
    @Override
    public void onUpdateData(){
        adapter.notifyDataSetUpdated();
    }

    private class MessageAdapter extends FilteredAdapter {
        public MessageAdapter(Context context, BaseObjectList list) {
            super(context, list);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final SMSCard card = (SMSCard) getData().get(position);
            view = this.getInflater().inflate(R.layout.message_item, parent, false);
            TextView tvText = (TextView) view.findViewById(R.id.tvTextMessage);

            String s  = card.getText();
            final int beg = s.indexOf("{Loc}");
            if(beg > 0) {
                String res = s.substring(0, beg) + " " + s.substring(beg + 5, s.length() - 1);
                tvText.setText(res);
            }
            else{
                tvText.setText(card.getText());
            }
            final ImageView ivOn = (ImageView) view.findViewById(R.id.ivMessageOn);
            final ImageView ivOff = (ImageView) view.findViewById(R.id.ivMessageOff);
            if(card.getState()) {
                ivOn.setVisibility(View.VISIBLE);
                ivOff.setVisibility(View.INVISIBLE);
            }
            else {
                ivOn.setVisibility(View.INVISIBLE);
                ivOff.setVisibility(View.VISIBLE);
            }
            ivOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OffAll();
                    card.setState(false);

                    notifyDataSetChanged();
                }
            });
            ivOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OffAll();
                    getMainActivity().selectSMS(card);
                    listener.onSelected(card.getID());
                    card.setState(true);
                    notifyDataSetChanged();
                }
            });
            return view;
        }
        public void OffAll(){
            for(int i=0;i<this.getCount();i++){
                SMSCard c = (SMSCard)getItem(i);
                c.setState(false);
            }
            notifyDataSetChanged();
        }
        public void notifyDataSetUpdated(){
            list = MainActivity.getMainActivity().getDBHelper().loadMessages();
            data = list;
            super.notifyDataSetChanged();
        }
    }

}
