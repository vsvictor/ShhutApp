package com.shhutapp.fragments.messages;

/**
 * Created by victor on 04.05.15.
 */
public interface MessageListListener {
    void onAdd();
    void onDelete();
    void onEdit(int id);
    void onSelected(int id);
}
