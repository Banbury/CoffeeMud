package com.planet_ink.coffee_mud.Abilities.Songs;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;


public class Song_Protection extends Song
{

	public Song_Protection()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="Protection";
		displayText="(Song of Protection)";
		miscText="";

		canBeUninvoked=true;
		isAutoinvoked=false;

		baseEnvStats().setLevel(15);
		quality=Ability.BENEFICIAL_OTHERS;

		baseEnvStats().setAbility(0);
		uses=Integer.MAX_VALUE;
		recoverEnvStats();
	}

	public Environmental newInstance()
	{
		return new Song_Protection();
	}
	public void affectEnvStats(Environmental affected, EnvStats affectableStats)
	{
		super.affectEnvStats(affected,affectableStats);
		if(invoker==null) return;
		affectableStats.setAttackAdjustment(affectableStats.attackAdjustment()-5);
	}


	public void affectCharStats(MOB affected, CharStats affectableStats)
	{
		super.affectCharStats(affected,affectableStats);
		if(invoker==null) return;
		affectableStats.setStat(CharStats.DEXTERITY,(int)Math.round(Util.div(affectableStats.getStat(CharStats.DEXTERITY),3.0)));
		affectableStats.setStat(CharStats.SAVE_ACID,affectableStats.getStat(CharStats.SAVE_ACID)+(invoker.charStats().getStat(CharStats.CHARISMA)*4));
		affectableStats.setStat(CharStats.SAVE_COLD,affectableStats.getStat(CharStats.SAVE_COLD)+(invoker.charStats().getStat(CharStats.CHARISMA)*4));
		affectableStats.setStat(CharStats.SAVE_ELECTRIC,affectableStats.getStat(CharStats.SAVE_ELECTRIC)+(invoker.charStats().getStat(CharStats.CHARISMA)*4));
		affectableStats.setStat(CharStats.SAVE_FIRE,affectableStats.getStat(CharStats.SAVE_FIRE)+(invoker.charStats().getStat(CharStats.CHARISMA)*4));
		affectableStats.setStat(CharStats.SAVE_GAS,affectableStats.getStat(CharStats.SAVE_GAS)+(invoker.charStats().getStat(CharStats.CHARISMA)*4));
	}
}
