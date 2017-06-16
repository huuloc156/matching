package com.rentracks.matching.listener;

import com.rentracks.matching.data.api.dto.chat.ChatItem;

/**
 * Created by HuuLoc on 6/13/17.
 */

public interface ListenMess {
    void MessageComing(ChatItem mess);
}
