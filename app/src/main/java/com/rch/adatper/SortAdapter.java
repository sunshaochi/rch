package com.rch.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.common.GlideUtils;
import com.rch.custom.SortModel;

import java.util.List;


public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	private int type;
	
	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}
	

	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.brand_adapter, null);
			viewHolder.brand_adapter_chat = (TextView) view.findViewById(R.id.brand_adapter_chat);
			viewHolder.brand_adapter_name = (TextView) view.findViewById(R.id.brand_adapter_name);
			viewHolder.brand_adapter_icon = (ImageView) view.findViewById(R.id.brand_adapter_icon);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		int section = getSectionForPosition(position);
		
		if(position == getPositionForSection(section)){
			viewHolder.brand_adapter_chat.setVisibility(View.VISIBLE);
			viewHolder.brand_adapter_chat.setText(mContent.getSortLetters());
		}else{
			viewHolder.brand_adapter_chat.setVisibility(View.GONE);
		}
	
		viewHolder.brand_adapter_name.setText(this.list.get(position).getName());
//		Glide.with(mContext).load(mContent.getBrandImagePath()).into(viewHolder.brand_adapter_icon);
        if(list.get(position).getName().equals("不限品牌")){
			viewHolder.brand_adapter_icon.setVisibility(View.GONE);
		}else {
			viewHolder.brand_adapter_icon.setVisibility(View.VISIBLE);
			GlideUtils.showImg(mContext, mContent.getBrandImagePath(), R.mipmap.banner, viewHolder.brand_adapter_icon);
		}
		return view;

	}
	


	final static class ViewHolder {
		TextView brand_adapter_chat;
		TextView brand_adapter_name;
		ImageView brand_adapter_icon;
	}


	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}