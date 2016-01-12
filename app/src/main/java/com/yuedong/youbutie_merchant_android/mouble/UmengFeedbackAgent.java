package com.yuedong.youbutie_merchant_android.mouble;

import android.content.Context;

import com.umeng.fb.FeedbackAgent;

/**
 * 把umeng FeedbackAgent对象更改成单例
 */
public class UmengFeedbackAgent {
    private static FeedbackAgent feedbackAgent = null;

    private UmengFeedbackAgent() {
    }

    public static FeedbackAgent getInstance(Context context) {
        if (feedbackAgent == null)
            feedbackAgent = new FeedbackAgent(context);
        return feedbackAgent;
    }
}
