package com.example.mycontactlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {

    private ArrayList<Contact> contactData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContact;
        public TextView textPhone;
        public Button deleteButton;
        public TextView textAddress2;
        public TextView textCity2;
        public TextView textState2;
        public TextView textZip2;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
            textAddress2 = itemView.findViewById(R.id.textAddress2);
            textCity2 = itemView.findViewById(R.id.textCity2);
            textState2 = itemView.findViewById(R.id.textState2);
            textZip2 = itemView.findViewById(R.id.textZip2);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
        public TextView getContactTextView() {

            return textViewContact;
        }

        public TextView getPhoneTextView() {

            return textPhone;
        }
        public Button getDeleteButton() {

            return deleteButton;
        }

        public TextView getAddressView() {
            return textAddress2;
        }
        public TextView getCityView() {
            return textCity2;
        }
        public TextView getStateView() {
            return textState2;
        }
        public TextView getZipView() {
            return textZip2;
        }

    }

    public ContactAdapter (ArrayList<Contact> arrayList, Context context) {

        contactData = arrayList;
        parentContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());
        cvh.getAddressView().setText(contactData.get(position).getStreetAddress());
        cvh.getCityView().setText(contactData.get(position).getCity());
        cvh.getStateView().setText(contactData.get(position).getState());
        cvh.getZipView().setText(contactData.get(position).getZipCode());
        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return contactData.size();
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Contact contact = contactData.get(position);
        ContactDataSource ds = new ContactDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getContactID());
            ds.close();
            if (didDelete) {
                contactData.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext,"Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }
}