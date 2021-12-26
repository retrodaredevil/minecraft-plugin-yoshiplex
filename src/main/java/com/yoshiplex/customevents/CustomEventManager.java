package com.yoshiplex.customevents;

import com.yoshiplex.customevents.messagecancel.MessageCancelListener;

public class CustomEventManager {

	public CustomEventManager(){
		new MessageCancelListener();
	}
	
}
