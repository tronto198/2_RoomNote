package com.example.roomnote.recyclervIewmodule;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

class RecyclerDistributor {

    private static String TAG = "RecyclerDistributor";

//    private static List<Integer> layoutList = new ArrayList<>();
    private static HashMap<String, Integer> typeNumMap = new HashMap<>();
    private static Vector<Integer> itemLayout = new Vector<>();
    private static int mSize = 0;

    static int getItemtype(IRecyclerItem rItem){
//        int itemType;
//        if (layoutList.contains(rItem.getLayoutRes())) {
//            itemType = layoutList.indexOf(rItem.getLayoutRes());
//        } else {
//            itemType = layoutList.size();
//            layoutList.add(rItem.getLayoutRes());
//        }
//        rItem.getClass().getName();
//        return itemType;

        String typeName = rItem.getClass().getName();
        int itemType;
        if(typeNumMap.containsKey(typeName)){
            itemType = typeNumMap.get(typeName);
        }
        else{
            typeNumMap.put(typeName, mSize);
            itemLayout.add(rItem.getLayoutRes());
            itemType = mSize++;
        }

        Log.d(TAG, "getItemtype() returned: " + typeName + " - " + itemType);
        return itemType;
    }

    static int getItemLayout(int itemType){
        return itemLayout.get(itemType);
    }

    static boolean isSame(String className, int itemType){
        return itemType == typeNumMap.get(className);
    }

    static boolean isSame(Class<IRecyclerItem> rItem, int itemType){
        Log.d(TAG, "isSame() called with: rItem = [" + rItem + "], itemType = [" + itemType + "]");
        Log.d(TAG, "isSame: rItem.getName: " + rItem.getName());
        return isSame(rItem.getName(), itemType);
    }

}
