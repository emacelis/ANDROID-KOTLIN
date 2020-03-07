package com.ejemplo.insert.database.mportafolio.Ui.tweets;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Constantes.SharedPreferencesManager;
import com.ejemplo.insert.database.mportafolio.Data.TweetViewModel;
import com.ejemplo.insert.database.mportafolio.R;
import com.ejemplo.insert.database.mportafolio.pojomodel.Like;
import com.ejemplo.insert.database.mportafolio.pojomodel.Tweet;

import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<Tweet> mValues;
    private Context ctx;
    private String username;
    private TweetViewModel tweetViewModel;

    public MyItemRecyclerViewAdapter(Context contexto,List<Tweet> items) {
        mValues = items;
        ctx = contexto;
        username= SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
        //SE INTANCIA AL TweetViewModel
        tweetViewModel= ViewModelProviders.of((FragmentActivity)ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues!=null){//si la lista de Tweets no es Vacia

            holder.mItem = mValues.get(position);
            holder.tvUserName.setText("@"+holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMensaje());
            holder.tvNumLikes.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photo=holder.mItem.getUser().getPhotoUrl();
            if(!photo.equals("")){
                Glide.with(ctx)
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/"+photo)
                        .into(holder.ivAvatar);
            }

            //Se resetean los colores
           Glide.with(ctx)
                    .load(R.drawable.ic_like_black)
                    .into(holder.ivLike);
            holder.tvNumLikes.setTextColor(ctx.getResources().getColor(R.color.black));//cambiar a otro color si es nuestro
            holder.tvNumLikes.setTypeface(null, Typeface.NORMAL);//marcar en negritas el contador de likes

            //SE verificva que si el usuario que ve el mensaje es el dueño para eleiminar id==id
            holder.ivShowMenu.setVisibility(View.GONE);
            if(holder.mItem.getUser().getUsername().equals(username)){
                holder.ivShowMenu.setVisibility(View.VISIBLE);
            }

            //SE CHECA EL ICONO DEL MENU
            holder.ivShowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweetViewModel.openDialogTweetMenu(ctx,holder.mItem.getId());
                }
            });

            //Se le da like al corazon
            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //se adapta el Tweeterviewmodel
                    //se le pasa el repositorio a l que se le dio like
                    tweetViewModel.likeTweet(holder.mItem.getId());
                }
            });

            for(Like like:holder.mItem.getLikes()) {
                if (like.getUsername().equals(username)) {
                    Glide.with(ctx)
                            .load(R.drawable.ic_like_pink)
                            .into(holder.ivLike);
                    holder.tvNumLikes.setTextColor(ctx.getResources().getColor(R.color.pink));//cambiar a otro color si es nuestro
                    holder.tvNumLikes.setTypeface(null, Typeface.BOLD);//marcar en negritas el contador de likes

                }
            }
        }
    }



    //REFRESCAR LOS TWEETS OBSERVER
    public void setData(List<Tweet> tweetList) {
        this.mValues=tweetList;
        notifyDataSetChanged();//Para refrescar la lista
    }

    @Override
    public int getItemCount() {
        //si values es disitinto de null
        if(mValues!=null){
            return mValues.size();
            //si values es igual a null
        }else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final ImageView ivShowMenu;
        public final TextView tvUserName;
        public final TextView tvMessage;
        public final TextView tvNumLikes;

        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAvatar = (ImageView) view.findViewById(R.id.imagenAvatar);
            ivLike = (ImageView) view.findViewById(R.id.likeTweet);
            tvUserName = (TextView) view.findViewById(R.id.UserName);
            tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvNumLikes = (TextView) view.findViewById(R.id.txLikes);
            ivShowMenu=(ImageView) view.findViewById(R.id.imageViewshowMenu);
        }


        //    public String toString() {
        //     return super.toString() + " '" + mContentView.getText() + "'";
        // }
    }
}
