package fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectwnavigation.R;

import models.ApiProvider;
import models.MyDataProvider;
import models.Persons;

public class Fragment_person_profile_info extends Fragment {
    private static final String P_ARG_ID = "argPersonId";

    TextView desc, contacts;
    Button btn_call;

    MyDataProvider provider;
    ApiProvider apiProvider;
    Context context;

    private int receivedId;

    public Fragment_person_profile_info() {
    }

    public static Fragment_person_profile_info newInstance(int id) {
        Fragment_person_profile_info fragment = new Fragment_person_profile_info();
        Bundle args = new Bundle();
        args.putInt(P_ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public void setContext(Context context) {
        this.provider = new MyDataProvider(context);
        apiProvider = new ApiProvider();
        this.context = context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            receivedId = getArguments().getInt(P_ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_profile_info, container, false);
    }

    Persons person = null;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String description = null;
        try {
            person = apiProvider.getPerson(receivedId);  // provider.getPerson(receivedId);

            description = person.getDesciption();

            contacts.setText(person.getNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }

        desc = view.findViewById(R.id.fragment_person_profile_info_desc);
        contacts = view.findViewById(R.id.fragment_person_profile_info_contacts);
        btn_call = view.findViewById(R.id.fragment_person_profile_info_btn_call);

        if (description == null) {
            description = "(Пользователь не указал о себе ничего)";
            desc.setText(description);
            desc.setTextColor(getResources().getColor(R.color.colorMutedText));
        } else {
            desc.setText(description);
        }

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                dialog.setTitle("Внимание!");
                dialog.setMessage("Вы уверены?");
                dialog.setPositiveButton("Да, позвонить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (person != null) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + person.getNumber()));
                                startActivity(intent);
                            } catch (Exception e) {
                                Log.e("showDialog_profile_info", e.getMessage());
                            }
                        }
                    }
                });

                dialog.setNegativeButton("Отменa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
