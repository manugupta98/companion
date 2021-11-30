package com.sdpd.companion.ui.groupstudy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdpd.companion.R;
import com.sdpd.companion.data.model.Group;

import java.util.ArrayList;

public class GroupStudyRecyclerViewAdapter extends RecyclerView.Adapter<GroupStudyRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "GroupStudyRecyclerView";

    private ArrayList<Group> groups;
    private Context context;


    public GroupStudyRecyclerViewAdapter(Context context) {
        this.context = context;
        groups = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group group = groups.get(position);

        Glide.with(context)
                .load(group.getImageUri())
                .placeholder(R.drawable.default_group_icon)
                .circleCrop()
                .into(holder.groupImageView);

        holder.groupTitleTextView.setText(group.getName());
        holder.groupDescriptionTextView.setText(group.getDescription());

        holder.groupTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick" + position);
//                NavController navController = Navigation.findNavController(view);
//                HomeFragmentDirections.ActionHomeFragmentToChatFragment action = HomeFragmentDirections.actionGroupStudyFragmentToGroupFragment(groups.get(position).getId());
//                navController.navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupTitleTextView;
        TextView groupDescriptionTextView;
        CardView groupTile;
        ImageView groupImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTitleTextView = itemView.findViewById(R.id.group_title);
            groupDescriptionTextView = itemView.findViewById(R.id.group_description);
            groupTile = itemView.findViewById(R.id.group_item_tile);
            groupImageView = itemView.findViewById(R.id.group_icon);
        }
    }

    public void setGroups(ArrayList<Group> newGroups) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return groups.size();
            }

            @Override
            public int getNewListSize() {
                return newGroups.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return groups.get(oldItemPosition).getId() ==
                        newGroups.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Group newGroup = newGroups.get(newItemPosition);
                Group oldGroup = groups.get(oldItemPosition);
                return newGroup.getId() == oldGroup.getId();
            }
        });
        groups = newGroups;
        result.dispatchUpdatesTo(this);
    }
}

