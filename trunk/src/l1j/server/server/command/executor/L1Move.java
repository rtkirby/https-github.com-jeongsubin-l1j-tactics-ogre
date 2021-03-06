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

import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Move implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Move.class.getName());

	private L1Move() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Move();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int locx = Integer.parseInt(st.nextToken());
			int locy = Integer.parseInt(st.nextToken());
			int before_locx = pc.getX();
			int before_locy = pc.getY();
			int before_mapid = pc.getMapId();
			short mapid;
			if (st.hasMoreTokens()) {
				mapid = Short.parseShort(st.nextToken());
			} else {
				mapid = pc.getMapId();
			}
			L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
			pc.sendPackets(new S_SystemMessage("座標(" + before_locx + ", "
					+ before_locy + ", " + before_mapid + ")から 座標(" + locx + ", "
					+ locy + ", " + mapid + ")に移動しました。"));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName
					+ " X座標 Y座標 [マップID] と入力して下さい。"));
		}
	}
}
