package br.com.thiengo.tcmaterialdesign.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.thiengo.tcmaterialdesign.R;
import br.com.thiengo.tcmaterialdesign.domain.ContextMenuItem;

/**
 * Created by viniciusthiengo on 7/19/15.
 */
public class ContextMenuAdapter extends BaseAdapter {
    private Context mContext;
    private List<ContextMenuItem> mList;
    private LayoutInflater mLayoutInflater;
    private int extraPadding;


    public ContextMenuAdapter(Context c, List<ContextMenuItem> l){
        mContext = c;
        mList = l;
        mLayoutInflater = LayoutInflater.from(c);

        float scale = c.getResources().getDisplayMetrics().density;
        extraPadding = (int)(8 * scale + 0.5f);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if( convertView == null ){
            convertView = mLayoutInflater.inflate(R.layout.context_menu, parent, false);
            holder = new ViewHolder();
            convertView.setTag( holder );

            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.tv_label);
            holder.vwDivider = convertView.findViewById(R.id.vw_divider);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivIcon.setImageResource( mList.get(position).getIcon() );
        holder.tvLabel.setText( mList.get(position).getLabel() );


        // BACKGROUND
            if( position == 0 ){
                ((ViewGroup) convertView).getChildAt(0).setBackgroundResource( R.drawable.context_menu_top_background );
            }
            else if( position == mList.size() - 1 ){
                ((ViewGroup) convertView).getChildAt(0).setBackgroundResource( R.drawable.context_menu_bottom_background );
            }
            else{
                ((ViewGroup) convertView).getChildAt(0).setBackgroundResource( R.drawable.context_menu_middle_background );
            }

        // H_LINE
            holder.vwDivider.setVisibility( position == mList.size() - 2 ? View.VISIBLE : View.GONE );

        // EXTRA PADDING
            ((ViewGroup) convertView).getChildAt(0).setPadding(0,
                    position == 0 || position == mList.size() - 1 ? extraPadding : 0,
                    0,
                    position == mList.size() - 1 ? extraPadding : 0);

        return convertView;
    }


    public static class ViewHolder{
        ImageView ivIcon;
        TextView tvLabel;
        View vwDivider;
    }
}
