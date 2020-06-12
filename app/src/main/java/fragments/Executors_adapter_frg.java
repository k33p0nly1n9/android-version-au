package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwnavigation.R;

import java.util.ArrayList;

import models.Executor;
import models.MyDataProvider;
import models.MyUtils;
import models.Persons;

public class Executors_adapter_frg extends RecyclerView.Adapter<Executors_adapter_frg.MyViewHolder> {
    private Context context;
    MyDataProvider provider;
    ArrayList<Executor> executors;

    Executors_adapter_frg(Context context, ArrayList<Executor> executors) {
        this.context = context;
        this.executors = executors;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, spcltn_txt,rating;
        ImageView photo;
        LinearLayout adapter_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.executors_adapter_frg_name);
            rating = itemView.findViewById(R.id.executors_adapter_frg_rating);
            spcltn_txt = itemView.findViewById(R.id.executors_adapter_frg_spec);
            photo = itemView.findViewById(R.id.executors_adapter_frg_photo);

            adapter_layout = itemView.findViewById(R.id.executors_adapter_frg_layout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_executors_adapter_row, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Executors_adapter_frg.MyViewHolder holder, final int position) {
        provider = new MyDataProvider(context);

        Executor executor = executors.get(position);

        holder.spcltn_txt.setText(executor.getSpecialztn());
        Persons p = provider.getPerson(executor.getPersonId());
        if (p.getPhoto() == null) {
            holder.photo.setImageResource(R.drawable.executors_default_image);
        } else {
            holder.photo.setImageBitmap(MyUtils.decodeByteToBitmap(p.getPhoto()));
        }
        holder.name.setText(p.getName());
        holder.rating.setText(p.getRating()+"");

        final int id = executor.getId();

        holder.adapter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == provider.getExecutorIdByPersonId(provider.getLoggedInPerson().getId())) {
                    Intent intent = new Intent(context, MyProfileActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, Executors_view_activity.class);
                    intent.putExtra("executorIdFragment", id);
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if (executors == null) {
            return 0;
        }
        return executors.size();
    }

}
