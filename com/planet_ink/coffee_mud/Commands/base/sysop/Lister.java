package com.planet_ink.coffee_mud.Commands.base.sysop;

import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import java.util.*;
import java.io.*;

public class Lister
{

	public StringBuffer reallyList(Hashtable these, int ofType)
	{
		return reallyList(these,ofType,null);
	}
	public StringBuffer reallyList(Hashtable these)
	{
		return reallyList(these,-1,null);
	}
	public StringBuffer reallyList(Hashtable these, Room likeRoom)
	{
		return reallyList(these,-1,likeRoom);
	}
	public StringBuffer reallyList(Vector these, int ofType)
	{
		return reallyList(these,ofType,null);
	}
	public StringBuffer reallyList(Vector these)
	{
		return reallyList(these,-1,null);
	}
	public StringBuffer reallyList(Vector these, Room likeRoom)
	{
		return reallyList(these,-1,likeRoom);
	}
	public StringBuffer reallyList(Hashtable these, int ofType, Room likeRoom)
	{
		StringBuffer lines=new StringBuffer("");
		if(these.size()==0) return lines;
		int column=0;
		for(Enumeration e=these.keys();e.hasMoreElements();)
		{
			String thisOne=(String)e.nextElement();
			Object thisThang=these.get(thisOne);
			boolean ok=true;
			if(ofType>=0)
			{
				if((thisThang!=null)&&(thisThang instanceof Ability))
				{
					if(((Ability)thisThang).classificationCode()!=ofType)
						ok=false;
				}
			}
			if((likeRoom!=null)&&(thisThang instanceof Room))
			{
				if((((Room)thisThang).ID().length()>0)&&(!((Room)thisThang).getArea().name().equals(likeRoom.getArea().name())))
				   ok=false;
			}
			if(ok)
			{
				if(++column>3)
				{
					lines.append("\n\r");
					column=1;
				}
				lines.append(Util.padRight(CMClass.className(thisThang),24));
			}
		}
		lines.append("\n\r");
		return lines;
	}

	public StringBuffer reallyList(Vector these, int ofType, Room likeRoom)
	{
		StringBuffer lines=new StringBuffer("");
		if(these.size()==0) return lines;
		int column=0;
		for(int i=0;i<these.size();i++)
		{
			Object thisThang=these.elementAt(i);
			boolean ok=true;
			if(ofType>=0)
			{
				if((thisThang!=null)&&(thisThang instanceof Ability))
				{
					if(((Ability)thisThang).classificationCode()!=ofType)
						ok=false;
				}
			}
			if(likeRoom!=null)
			{
				if((((Room)thisThang).ID().length()>0)&&(!((Room)thisThang).getArea().name().equals(likeRoom.getArea().name())))
				   ok=false;
			}
			if(ok)
			{
				if(++column>3)
				{
					lines.append("\n\r");
					column=1;
				}
				lines.append(Util.padRight(CMClass.className(thisThang),24));
			}
		}
		lines.append("\n\r");
		return lines;
	}
	public StringBuffer roomDetails(Vector these, Room likeRoom)
	{
		StringBuffer lines=new StringBuffer("");
		if(these.size()==0) return lines;
		if(likeRoom==null) return lines;
		for(int m=0;m<these.size();m++)
		{
			Room thisThang=(Room)these.elementAt(m);
			String thisOne=(String)thisThang.ID();
			if((thisOne.length()>0)&&(thisThang.getArea().name().equals(likeRoom.getArea().name())))
				lines.append(Util.padRight(thisOne,24)+": "+thisThang.displayText()+"\n\r");
		}
		lines.append("\n\r");
		return lines;
	}

	public StringBuffer listSessions(MOB mob)
	{
		StringBuffer lines=new StringBuffer("^Xstatus | Valid | Name             | Location             ^?\n\r");
		for(int s=0;s<Sessions.size();s++)
		{
			Session thisSession=(Session)Sessions.elementAt(s);

			lines.append ( thisSession.killFlag() ? "^HCLOSING^?|" : " open  |" );

			if (thisSession.mob() != null)
			{
				lines.append ( (thisSession.mob().session() == thisSession) ? "  yes  " : "  ^HNO!^? " );
				lines.append ("[^B" + Util.padRight(thisSession.mob().name(),18) + "^?] " );
				if ( thisSession.mob().location() != null )
					lines.append ( thisSession.mob().location().ID() );
				else
					lines.append ( "^B(no location)^?" );
			}
			else
				lines.append ( "      ^B-nameless, mobless-^?" );
			lines.append ("\n\r");
			lines.append ("            IP: " + thisSession.getAddress() + "\n\r");
		}
		return lines;
	}

	public void dumpThreadGroup(StringBuffer lines,ThreadGroup tGroup)
	{
		int ac = tGroup.activeCount();
		int agc = tGroup.activeGroupCount();
		Thread tArray[] = new Thread [ac+1];
		ThreadGroup tgArray[] = new ThreadGroup [agc+1];

		tGroup.enumerate(tArray,false);
		tGroup.enumerate(tgArray,false);

		lines.append(" ^HTGRP^?  ^H" + tGroup.getName() + "^?\n\r");

		for (int i = 0; i<ac; ++i)
		{
			if (tArray[i] != null)
			{
				lines.append(tArray[i].isAlive()? "  ok   " : " BAD!  ");
				lines.append(tArray[i].getName() + "\n\r");
			}
		}

		if (agc > 0)
		{
			lines.append("{\n\r");
			for (int i = 0; i<agc; ++i)
			{
				if (tgArray[i] != null)
					dumpThreadGroup(lines,tgArray[i]);
			}
			lines.append("}\n\r");
		}
	}


	public StringBuffer listThreads(MOB mob)
	{
		StringBuffer lines=new StringBuffer("^XStatus|Name                 ^?\n\r");
		try
		{
			ThreadGroup topTG = Thread.currentThread().getThreadGroup();
			while (topTG != null && topTG.getParent() != null)
				topTG = topTG.getParent();
			if (topTG != null)
				dumpThreadGroup(lines,topTG);

		}
		catch (Exception e)
		{
			lines.append ("\n\rBastards! Exception while listing threads: " + e.getMessage() + "\n\r");
		}
		return lines;

	}

	public void list(MOB mob, Vector commands)
	{
		if(commands.size()==0)
		{
			mob.tell("List what?");
			return;
		}

		String listThis=Util.combine(commands,0).toUpperCase();
		String listWord=((String)commands.firstElement()).toUpperCase();

		if("ITEMS".startsWith(listThis))
			mob.tell(reallyList(CMClass.items).toString());
		else
		if("ARMOR".startsWith(listThis))
			mob.tell(reallyList(CMClass.armor).toString());
		else
		if("WEAPONS".startsWith(listThis))
			mob.tell(reallyList(CMClass.weapons).toString());
		else
		if("MOBS".startsWith(listThis))
			mob.tell(reallyList(CMClass.MOBs).toString());
		else
		if("ROOMS".startsWith(listThis))
			mob.tell(roomDetails(CMMap.map,mob.location()).toString());
		else
		if("AREA".startsWith(listThis))
			mob.tell(reallyList(CMMap.map,mob.location()).toString());
		else
		if("LOCALES".startsWith(listThis))
			mob.tell(reallyList(CMClass.locales).toString());
		else
		if("BEHAVIORS".startsWith(listThis))
			mob.tell(reallyList(CMClass.behaviors).toString());
		else
		if("EXITS".startsWith(listThis))
			mob.tell(reallyList(CMClass.exits).toString());
		else
		if("RACES".startsWith(listThis))
			mob.tell(reallyList(CMClass.races).toString());
		else
		if("CLASSES".startsWith(listThis))
			mob.tell(reallyList(CMClass.charClasses).toString());
		else
		if("SPELLS".startsWith(listThis))
			mob.tell(reallyList(CMClass.abilities,Ability.SPELL).toString());
		else
		if("SONGS".startsWith(listThis))
			mob.tell(reallyList(CMClass.abilities,Ability.SONG).toString());
		else
		if("PRAYERS".startsWith(listThis))
			mob.tell(reallyList(CMClass.abilities,Ability.PRAYER).toString());
		else
		if("PROPERTIES".startsWith(listThis))
			mob.tell(reallyList(CMClass.abilities,Ability.PROPERTY).toString());
		else
		if("THIEFSKILLS".startsWith(listThis))
			mob.tell(reallyList(CMClass.abilities,Ability.THIEF_SKILL).toString());
		else
		if("SKILLS".startsWith(listThis))
			mob.tell(reallyList(CMClass.abilities,Ability.SKILL).toString());
		else
		if("TICKS".startsWith(listThis))
			mob.tell(ExternalPlay.listTicks(-1).toString());
		else
		if("TICKS".startsWith(listWord))
			mob.tell(ExternalPlay.listTicks(Util.s_int(Util.combine(commands,1))).toString());
		else
		if("MAGIC".startsWith(listThis))
			mob.tell(reallyList(CMClass.miscMagic).toString());
		else
		if("LOG".startsWith(listThis))
			mob.tell(Log.getLog().toString());
		else
		if("USERS".startsWith(listThis))
			ExternalPlay.listUsers(mob);
		else
		if("SESSIONS".startsWith(listThis))
			mob.tell(listSessions(mob).toString());
		else
		if("THREADS".startsWith(listThis))
			mob.tell(listThreads(mob).toString());
		else
			mob.tell("Can't list those, try ITEMS, ARMOR, WEAPONS, MOBS, ROOMS, LOCALES, EXITS, RACES, CLASSES, MAGIC, SPELLS, SONGS, PRAYERS, BEHAVIORS, SKILLS, THIEFSKILLS, PROPERTIES, TICKS, LOG, USERS, SESSIONS, THREADS or AREA.");
	}
}
