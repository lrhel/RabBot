package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class ActiveCommand	implements CommandExecutor {
		@Command(aliases = {"active"}, privateMessages = false, description = "Ping someone and send active message!", showInHelpPage = false)
		public String onActiveCommand(String[] arg, User users, Server server, TextChannel ch, Message message) {
			if (users.isBot()) { return ""; }

			Random rng = new Random(System.currentTimeMillis());
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			Date date = new Date();
			System.out.println("[" + df.format(date) + "] " + users.getName() + ": rb.active " + String.join(" ", arg));
			/*if(!users.getIdAsString().contentEquals("201426733637828608")
			   && !users.isBotOwner()) {
			    return "";
			    }*/
			boolean hej = false;
			for(Role r : users.getRoles(server)) {
			    if(r.getIdAsString().contentEquals("484422447597092884")) {
				hej = true;
				break;
			    }
			}
			if(!hej && !users.isBotOwner()) {
			    return "";
			}
					
			if(users.getName().contains("....")) {
				try {
					server.getApi().getOwner().get().sendMessage(users.getIdAsString());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				return users.getMentionTag() + " fuck ur mom nigga bitch";
			}
			else if (users.getIdAsString().contentEquals("284369381608128515"))
				return "sad . . .";
			if(arg.length == 0) {
				ArrayList<User> userList = new ArrayList<User>(server.getMembers());
				String msg = "<@" + userList.get(rng.nextInt(userList.size())).getIdAsString() + "> y so active xdxdxd";
				ch.sendMessage(msg);
			}
			else {
				int arg_i;
				try {
					arg_i = Integer.parseInt(arg[arg.length - 1]);
					if(arg_i > 10)
						arg_i = 10;
				} catch(NumberFormatException e) {
					arg_i = 0;
				} catch(ArrayIndexOutOfBoundsException e) {
					arg_i = -1;
				} 
				if(arg_i == -1) {
					try {
						return server.getApi().getOwner().get().getMentionTag() + " Error on command. . . " + String.join(" ", arg);
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
						return "Error on Error... rip rip";
					}
				}
				
				ArrayList<User> userList = new ArrayList<User>(message.getMentionedUsers());
				ArrayList<Role> userRole = new ArrayList<Role>(message.getMentionedRoles());
				if(userList.size() != 0 || userRole.size() != 0) {
					for(User usr : userList) {
			    		String msg = usr.getMentionTag() + " y so active xdxdxd";
			    		ch.sendMessage(msg);
			    		break;				    	
				    }
					for (Role role : userRole) {
						String msg = role.getMentionTag() + " y so active xdxdxd";
						ch.sendMessage(msg);
						break;
					}
				}
				else {
					userList = new ArrayList<User>(server.getMembers());
					if(arg_i == 0)
						arg_i = 1;
					if(arg_i == 1) {
						for(String args : arg) {
							for(User usr : userList) {
						    	if(usr.getName().toLowerCase().contains(args.toLowerCase()) || (usr.getNickname(server).isPresent() && usr.getNickname(server).get().toLowerCase().contains(args.toLowerCase()))) {
						    		String msg = usr.getMentionTag() + " y so active xdxdxd";
						    		for(int j = 0; j < arg_i; j++)
						    			ch.sendMessage(msg);
						    		break;
						    	}
						    }
						}
					}
					else {
						for(int i = 0; i < (arg.length - 1); i++) {
							for(User usr : userList) {
						    	if(usr.getName().toLowerCase().contains(arg[i].toLowerCase()) || (usr.getNickname(server).isPresent() && usr.getNickname(server).get().toLowerCase().contains(arg[i].toLowerCase()))) {
						    		String msg = usr.getMentionTag() + " y so active xdxdxd";
						    		for(int j = 0; j < arg_i; j++)
						    			ch.sendMessage(msg);
						    		break;
						    	}
						    }
						}
					}
				}
			}
/* 
 * Old version 			
			String msg;
				int i;
				try {
					i = Integer.parseInt(arg[1]);
				} catch (NumberFormatException e) {
					i = -1;
				}
				if(i == 0)
					return "";
				if(i >= 100)
				    i = 100;
				if(userList.size() == 1 && i != -1) {
					for(User usr : userList) {
						for(int j = 0; j < i; j++) {
							msg = usr.getMentionTag() + " y so active xdxdxd";
							ch.sendMessage(msg);
						}
					}
				} else if (userList.size() == 0 && i != -1){
					userList = new ArrayList<User>(server.getMembers());
					for(int j = 0; j < i; j++) {
					    for(User usr : userList) {
					    	if(usr.getName().toLowerCase().contains(arg[0].toLowerCase()) || (usr.getNickname(server).isPresent() && usr.getNickname(server).get().toLowerCase().contains(arg[0].toLowerCase()))) {
					    		msg = usr.getMentionTag() + " y so active xdxdxd";
					    		ch.sendMessage(msg);
					    		break;
					    	}
					    }
					}
				} else if(userList.size() == 2) {
					if(userList.size() != 0) {					
						for(User usr : userList) {
							msg = usr.getMentionTag() + " y so active xdxdxd";
							ch.sendMessage(msg);
						}
					}
				}
				else {
					userList = new ArrayList<User>(server.getMembers());
					for(String args : arg) {
				    	for(User usr : userList) {
				    		if(usr.getName().toLowerCase().contains(args.toLowerCase()) || (usr.getNickname(server).isPresent() && usr.getNickname(server).get().toLowerCase().contains(args.toLowerCase()))) {
				    			msg = usr.getMentionTag() + " y so active xdxdxd";
				    			ch.sendMessage(msg);
				    			break;
				    		}
				    	}
					}
				}	
			} else {
				ArrayList<User> userList = new ArrayList<User>(message.getMentionedUsers());
				String msg;
				if(userList.size() != 0) {					
					for(User usr : userList) {
						msg = usr.getMentionTag() + " y so active xdxdxd";
						ch.sendMessage(msg);
					}
				}
				else {
					userList = new ArrayList<User>(server.getMembers());
					for(String args : arg) {
					    for(User usr : userList) {
					    	if(usr.getName().toLowerCase().contains(args.toLowerCase()) || (usr.getNickname(server).isPresent() && usr.getNickname(server).get().toLowerCase().contains(args.toLowerCase()))) {
					    		msg = usr.getMentionTag() + " y so active xdxdxd";
					    		ch.sendMessage(msg);
					    		break;
					    	}
					    }
					}
				}
				}
*/
			return "";
		}
}
