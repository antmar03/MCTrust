package me.uraniumape.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigValues {
	
	//Messages
	public static String permission_message;
	public static String withdraw_message;
	public static String deposit_message;
	public static String transfer_message;
	public static String shop_name;
	public static String pay_with_cash;
	public static String pay_with_debit;
	public static String shop_id;
	public static String take_one;
	public static String take_ten;
	public static String take_max;
	public static String add_one;
	public static String add_ten;
	public static String add_max;
	public static String finish_transaction;
	public static String amount_to_withdraw;
	public static String withdraw_money;
	public static String deposit_money;
	public static String transfer_to_player;
	public static String withdraw_a_cheque;
	public static String bank_teller;
	public static String atm;
	public static String cheque;
	public static String withdraw_at_bank;
	public static String purchase_message;
	public static String price;
	public static String purchase_with_debit;
	public static String purchase_with_cash;
	public static String not_enough_cash;
	public static String not_enough_bank;
	public static String amount;
	public static String balance;
	public static String transfer_message_from;
	public static String bank_fee;
	
	//Color values
	public static String msg_color_positive;
	public static String msg_color_negative;
	public static String cash_color;
	
	//Features
	public static boolean enable_onlinebank;
	
	//Etc
	public static String currency_name;

	
	public static void initializeValues() {
		FileConfiguration config = MCTrust.getInstance().getConfig();
		
		//Messages
		permission_message = config.getString("messages.permission-message");
		withdraw_message = config.getString("messages.withdraw-message");
		deposit_message = config.getString("messages.deposit-message");
		transfer_message = config.getString("messages.transfer-message");
		purchase_message = config.getString("messages.purchase-message");
		shop_name = config.getString("messages.shop-name");
		pay_with_cash = config.getString("messages.pay-with-cash");
		pay_with_debit = config.getString("messages.pay-with-debit");
		shop_id = config.getString("messages.shop-id");
		take_one = config.getString("messages.take-one");
		take_ten = config.getString("messages.take-ten");
		take_max = config.getString("messages.take-max");
		add_one = config.getString("messages.add-one");
		add_ten = config.getString("messages.add-ten");
		add_max = config.getString("messages.add-max");
		balance = config.getString("messages.balance");
		finish_transaction = config.getString("messages.finish-transaction");
		amount_to_withdraw = config.getString("messages.amount-to-withdraw");
		withdraw_money = config.getString("messages.withdraw-money");
		deposit_money = config.getString("messages.deposit-money");
		transfer_to_player = config.getString("messages.transfer-to-player");
		withdraw_a_cheque = config.getString("messages.withdraw-a-cheque");
		price = config.getString("messages.price");
		purchase_with_debit = config.getString("messages.purchase-with-debit");
		purchase_with_cash = config.getString("messages.purchase-with-cash");
		not_enough_cash = config.getString("messages.not-enough-cash");
		not_enough_bank = config.getString("messages.not-enough-bank");
		amount = config.getString("messages.amount");
		transfer_message_from = config.getString("messages.transfer-message-from");
		bank_fee = config.getString("messages.bank-fee");
		
		//Features
		enable_onlinebank = config.getBoolean("features.enable-onlinebank");
		
		//Colors
		msg_color_positive = config.getString("colors.message-color-positive");
		msg_color_negative = config.getString("colors.message-color-negative");
		cash_color = config.getString("colors.cash-color");
		
		//Misc
		currency_name = config.getString("etc.currency-name");
		
	}
	
	
	
	
}
