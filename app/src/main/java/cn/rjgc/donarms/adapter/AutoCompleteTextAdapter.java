package cn.rjgc.donarms.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.rjgc.commonlib.util.PixelUtil;
import cn.rjgc.commonlib.util.Util;
import cn.rjgc.commonlib.util.eventbus.LiveDataBus;
import cn.rjgc.donarms.R;
import cn.rjgc.donarms.bean.OilStationBean;
import cn.rjgc.donarms.util.EventType;

/**
 * Date 2021/9/27
 *
 * @author Don
 */
public class AutoCompleteTextAdapter extends BaseAdapter implements Filterable {
    private List<OilStationBean> mData;

    private Context mContext;

    private ReceiverFilter myFilter;


    /**
     * 输入框中当前的关键字
     */
    private String tempKeyString;

    public AutoCompleteTextAdapter(List<OilStationBean> data, Context context) {
        super();
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public OilStationBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_dropdown_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.tv_value);
            viewHolder.address = convertView.findViewById(R.id.tv_address);
            viewHolder.bottomLine = convertView.findViewById(R.id.tv_bottom_line);
            viewHolder.textView.setTextColor(Util.getColor(convertView.getContext(), R.color.black));
            viewHolder.address.setTextColor(Util.getColor(convertView.getContext(), R.color.black));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String temp1 = mData.get(position).getName();
        SpannableString spannableString = new SpannableString(temp1);
        if (!TextUtils.isEmpty(temp1) && !TextUtils.isEmpty(tempKeyString)) {
            spannableString.setSpan(new ForegroundColorSpan(Util.getColor(viewHolder.textView.getContext(), R.color.teal_200)),
                    temp1.indexOf(tempKeyString),
                    temp1.indexOf(tempKeyString) + tempKeyString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        viewHolder.textView.setText(spannableString);
        viewHolder.address.setText(mData.get(position).getAddress());
        if (position >= mData.size() - 1) {
            viewHolder.bottomLine.setVisibility(View.GONE);
        } else {
            viewHolder.bottomLine.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        // 自定义的拦截器，对包含的关键字进行处理
        if (null == myFilter) {
            myFilter = new ReceiverFilter();
        }
        return myFilter;
    }

    class ReceiverFilter extends Filter {
        private OilStationBean[] strsContains;

        public ReceiverFilter() {
            super();
            if (strsContains == null) {
                strsContains = new OilStationBean[mData.size()];
            }
            for (int i = 0; i < mData.size(); i++) {
                strsContains[i] = mData.get(i);
            }

        }

        /**
         * 在这个方法里执行过滤方法
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // filterREsults是filter的一个结果对象，里面只包括两个成员属性，Object 和 count
            FilterResults result = new FilterResults();

            // 定义一个集合来保存数组中存在的关键字的字符串
            ArrayList<OilStationBean> strsTemp = new ArrayList<OilStationBean>();

            // 在这里可以获取autoCompeted中输入的信息
            // 把字符串中包含这个关键字的item返回给adapter.
            if (null != constraint && constraint.length() > 0) {
                for (int i = 0; i < strsContains.length; i++) {
                    String tempstr = strsContains[i].getName();
                    // 同一做大小写的处理
                    if (tempstr.toLowerCase().contains(constraint.toString()
                            .toLowerCase()))// 包含关键字的添加进去
                    {
                        strsTemp.add(strsContains[i]);
                    }
                }
                result.values = strsTemp;
                result.count = strsTemp.size();
            }

            // 这个结果集 将会返回给 publishResults 方法中的 FilterResults results这个参数 所以我们在下面获取
            return result;
        }

        /**
         * 在这个方法里发布筛选过后得到的数据同时更新Adapter更新
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            ArrayList<OilStationBean> tempList = (ArrayList<OilStationBean>) results.values;
            if (null != tempList) {
                // if (tempList.size() <= 0) {
                // LiveDataBus.get().with(EventType.CLOSE_DIALOG, Boolean.class).postValue(true);
                // } else {
                mData = tempList;
                // 这个时候输入的关键字
                tempKeyString = constraint.toString();
                notifyDataSetChanged();
                // LiveDataBus.get().with(EventType.CLOSE_DIALOG, Boolean.class).postValue(false);
                // }
            } else {
                tempKeyString = "";
                mData = Arrays.asList(strsContains);
                notifyDataSetChanged();
                LiveDataBus.get().with(EventType.CLOSE_DIALOG, Boolean.class).postValue(false);
            }

        }

    }

    class ViewHolder {
        private TextView textView;
        private TextView address;
        private View bottomLine;
    }
}
