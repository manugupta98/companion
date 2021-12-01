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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdpd.companion.R;
import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.model.User;

import java.util.ArrayList;

public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MembersRecyclerViewAdapter";

    private ArrayList<User> members;
    private Context context;


    public MembersRecyclerViewAdapter(Context context) {
        this.context = context;
        members = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        MembersRecyclerViewAdapter.ViewHolder viewHolder = new MembersRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersRecyclerViewAdapter.ViewHolder holder, int position) {
        User member = members.get(position);

        Glide.with(context)
                .load(member.getPhotoUri())
                .placeholder(R.drawable.default_group_icon5)
                .circleCrop()
                .into(holder.memberImageView);

        holder.memberNameTextView.setText(member.getDisplayName());

//        holder.groupTile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onclick" + position);
//                NavController navController = Navigation.findNavController(view);
//                GroupStudyFragmentDirections.ActionGroupStudyFragmentToGroupInfoFragment action = GroupStudyFragmentDirections.actionGroupStudyFragmentToGroupInfoFragment(group.getId(), group.getName());
//                navController.navigate(action);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView memberNameTextView;
        CardView memberTile;
        ImageView memberImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberTile = itemView.findViewById(R.id.user_item_tile);
            memberImageView = itemView.findViewById(R.id.user_icon);
            memberNameTextView = itemView.findViewById(R.id.user_name);
        }
    }

    public void setMembers(ArrayList<User> newMembers) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return members.size();
            }

            @Override
            public int getNewListSize() {
                return newMembers.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return members.get(oldItemPosition).getUid().equals(newMembers.get(newItemPosition).getUid());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                User newMember = newMembers.get(newItemPosition);
                User oldMember = members.get(oldItemPosition);
                return newMember.getUid().equals(oldMember.getUid());
            }
        });
        members = newMembers;
        result.dispatchUpdatesTo(this);
    }
}


