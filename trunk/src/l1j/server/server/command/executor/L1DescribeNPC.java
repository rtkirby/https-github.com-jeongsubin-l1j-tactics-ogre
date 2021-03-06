/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.command.executor;

import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillTable;
import l1j.server.server.model.Attribute;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1MobSkill;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skill;

public class L1DescribeNPC implements L1CommandExecutor
{
	private static Logger _log = Logger.getLogger(L1DescribeNPC.class.getName());

	private L1DescribeNPC()
	{
	}

	public static L1CommandExecutor getInstance()
	{
		return new L1DescribeNPC();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg)
	{
		try
		{
			StringBuilder msg = new StringBuilder();
			final String BR = System.getProperty("line.separator");
			int hpr = 0, mpr = 0;

    		StringTokenizer st = new StringTokenizer(arg);
    		String targetName = st.nextToken();
			int npcId = NpcTable.getInstance().findNpcIdByNameWithoutSpace(targetName);
			L1Npc npc = NpcTable.getInstance().getTemplate(npcId);
			List<L1MobSkill> skills = MobSkillTable.getInstance().get(npcId);

			msg.append("-- ?????????????????? --" + BR);
			msg.append(" " + BR);
			msg.append("??????:" + npc.get_name() + (npc.getBoss()? "(??????)" : "") + BR);
			msg.append("??????:" + npc.get_family() + BR);
			msg.append("?????????:" + npc.get_level() + "(??" + npc.get_randomlevel() + ")" + BR);
			msg.append("?????????:" + npc.get_lawful() + "(??" + npc.get_randomlawful() + ")" + BR);
			msg.append("?????????:" + npc.get_exp() + "(??" + npc.get_randomexp() + ")" + BR);
			msg.append("NPCID:" + npc.get_npcId() + BR);
			msg.append("GFXID:" + npc.get_gfxid() + BR);
			msg.append("S/B:" + npc.get_size());
			msg.append(" " + BR);

			msg.append("-- ??????????????? --" + BR);
			msg.append(" " + BR);
			msg.append("STR:" + npc.get_str() + " ");
			msg.append("DEX:" + npc.get_dex() + " ");
			msg.append("INT:" + npc.get_int() + " ");
			msg.append(" " + BR);
			msg.append("CON:" + npc.get_con() + " ");
			msg.append("WIS:" + npc.get_wis() + " ");
			msg.append(" " + BR);
			msg.append("AC:" + npc.get_ac() + "(??" + npc.get_randomac() + ") ");
			msg.append("MR:" + npc.get_mr() + " ");
			msg.append(" " + BR);
			msg.append("HP: " + npc.get_hp() + "(??" + npc.get_randomhp() + ") ");
			msg.append("MP: " + npc.get_mp() + "(??" + npc.get_randommp() + ") ");
			msg.append(" " + BR);
			msg.append("HPR: " + npc.get_hpr() + "(" + npc.get_hprinterval() / (double)1000.0+ "???) ");
			msg.append("MPR: " + npc.get_mpr() + "(" + npc.get_mprinterval() / (double)1000.0+ "???) ");
			msg.append(" " + BR + BR);
			msg.append("????????????:" + npc.get_atkspeed() + BR);
			msg.append("????????????????????????:" + npc.getAltAtkSpeed() + BR);
			msg.append("????????????:" + npc.getAtkMagicSpeed() + BR);
			msg.append("????????????????????????:" + npc.getSubMagicSpeed() + BR);
			msg.append("????????????:" + npc.get_passispeed() + BR);
			msg.append("??????????????????:" + npc.get_damagereduction() + BR);
			msg.append("????????????:" + npc.get_ranged() + BR);
			msg.append("????????????????????????:" + ((npc.get_IsTU() == true)? "???" : "??????") + BR);
			msg.append("????????????:" + ((npc.get_IsErase() == true)? "???" : "??????") + BR);
			msg.append("????????????:" + npc.get_paralysisatk() + BR);
			msg.append("?????????:" + npc.get_poisonatk() + BR);
			msg.append(" " + BR);
			msg.append("??????:" + ((npc.get_undead() == 1)? "???????????????" : "??????") + BR);
			msg.append("????????????:" + Attribute.getAttribute(npc.get_weakAttr()) + BR);
			msg.append(" " + BR);
			msg.append("[?????????]" + BR);
			int count = 0;
			for(int i = 0; i < skills.size();++i)
			{
				L1Skill skill = null;
				String skillName = null;
				int skillType = skills.get(i).getType();
				if(skillType >= 2)
				{
					skill = SkillTable.getInstance().findBySkillId(skills.get(i).getSkillId());
					skillName = (skillType == 3)? "?????????" : skill.getName();
					++count;
					msg.append("No." + (count) + " " + skillName + BR);
				}
			}

			pc.sendPackets(new S_SystemMessage(msg.toString()));
		}
		catch (Exception e)
		{
			pc.sendPackets(new S_SystemMessage(cmdName + " ?????????????????????"));
		}
	}
}
