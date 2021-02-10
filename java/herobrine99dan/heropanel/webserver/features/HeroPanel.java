package herobrine99dan.heropanel.webserver.features;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.sun.management.OperatingSystemMXBean;

import herobrine99dan.heropanel.ReflectionUtility;
import herobrine99dan.heropanel.UniportWebServer;
import herobrine99dan.heropanel.protocol.HTTPRequestEvent;

public class HeroPanel {

	private final AuthenticationHandler authHandler;
	private final ConsoleHandler consoleHandler;
	private final TPSHandler tpsHandler;
	public static final boolean logFromFile = true;
	private final UniportWebServer main;

	public HeroPanel(ReflectionUtility reflection, UniportWebServer main) {
		this.main = main;
		authHandler = new AuthenticationHandler(main.getHeroPanelConfig().TOTPKey());
		consoleHandler = new ConsoleHandler();
		if (logFromFile) {
			consoleHandler.schedule(main);
		}
		tpsHandler = new TPSHandler();
		tpsHandler.startTask(reflection, main);
	}

	public void handleThePages(HTTPRequestEvent event) throws IOException, GeneralSecurityException {
		String req = URLDecoder.decode(event.getConnection().request, StandardCharsets.UTF_8.name())
				.replaceFirst("/panel", "");
		String ip = event.getAddress().getAddress().getHostAddress();
		if (event.getHost().contains("ngrok.io")) {
			ip = event.getNgrokIp();
		}
		if (req.equals(" ") || req.equals("/") || req.equals("/index.html") || req.equals("")) {
			if (authHandler.isAuthenticated(ip)) {
				event.setMessage("<meta http-equiv=\"refresh\" content=\"0; URL='/panel/dashboard.html'\" />\n" + "");
			} else {
				event.setImage(Files
						.readAllBytes(new File(main.getDataFolder(), "heropanel-login.html").toPath()));
			}
		}
		String argument = "";
		String argumentValue = "";
		if (req.contains("?")) {
			int j = req.indexOf('?');
			argument = req.substring(j + 1);
			int importantCharPosition = argument.indexOf("=");
			argumentValue = argument.substring(importantCharPosition + 1);
			if (argument.contains("=")) {
				argument = argument.substring(0, importantCharPosition);
			}
			req = req.substring(0, j);
		}
		if (argument.equals("code") && !authHandler.isAuthenticated(ip)) {
			boolean login = authHandler.loginHandler(ip, argumentValue);
			if (login) {
				event.setMessage("<meta http-equiv=\"refresh\" content=\"0; URL='/panel/dashboard.html'\" />\n" + "");
			} else {
				event.setMessage("<meta http-equiv=\"refresh\" content=\"0; URL='/panel/'\" />\n" + "");
			}
			return;
		}
		if (argument.equals("quit") && authHandler.isAuthenticated(ip)) {
			authHandler.logoutHandler();
			event.setMessage("<meta http-equiv=\"refresh\" content=\"0; URL='/panel/'\" />\n" + "");
			return;
		}
		if (req.equals("/dashboard.html") && authHandler.isAuthenticated(ip)) {
			event.setImage(Files
					.readAllBytes(new File(main.getDataFolder(), "heropanel-dashboard.html").toPath()));
			return;
		} else if (req.startsWith("/dashboard.html") && !authHandler.isAuthenticated(ip)) {
			event.setMessage("<meta http-equiv=\"refresh\" content=\"0; URL='/panel/'\" />\n" + "");
			return;
		}
		if (req.equals("/server") && authHandler.isAuthenticated(ip)) {
			event.setMessage(this.getDashBoardApi());
			return;
		}
		if (req.equals("/server/command") && authHandler.isAuthenticated(ip)) {
			final String argumentFinal = argument;
			final String argumentFinalValue = argumentValue;
			new BukkitRunnable() {
				@Override
				public void run() {
					if (argumentFinal.equals("sendcmd")) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), argumentFinalValue);
					}
				}
			}.runTask(main);
			event.setMessage("Done!");
			return;
		}
		if (req.equals("/server/consolelog") && authHandler.isAuthenticated(ip)) {
			event.setMessage(this.getConsoleHandler().getLogToSendInJSONForm());
			return;
		}
		if (req.equals("/server/fullconsolelog") && authHandler.isAuthenticated(ip)) {
			if (logFromFile) {
				event.setMessage(this.getConsoleHandler().getFullLogToSendInJSONForm());
			} else {
				event.setMessage("{}");
			}
			return;
		}
	}

	private static final String dashboardjson = "{\"Status\":1,\"GameType\":\"#gamemode\",\"GameId\":\"#gameid\",\"Version\":\"#version\",\"Map\":\"#map\",\"MaxPlayers\":#maxplayer,\"NumPlayers\":#numplayers,\"Motd\":\"#motd\",\"Tps\":#tps,\"StartTime\":#starttime,\"Memory\":{\"Total\":#memtotal,\"Used\":#memused,\"Free\":#memfree,\"ActualFree\":0,\"ActualUsed\":0},\"CPU\": #cpu}";

	public String getDashBoardApi() {
		String json = dashboardjson;
		long RAM_USED = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long cpu = Math
				.round(ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getSystemCpuLoad() * 100);
		float tps = this.tpsHandler.getTPS();
		String ip = "127.0.0.1";
		return json.replace("#numplayers", Bukkit.getOnlinePlayers().size() + "")
				.replace("#maxplayer", Bukkit.getMaxPlayers() + "").replace("#memused", RAM_USED + "")
				.replace("#memtotal", Runtime.getRuntime().totalMemory() + "").replace("#cpu", cpu + "")
				.replace("#tps", tps + "")
				.replace("#version", Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-")))
				.replace("#map", Bukkit.getWorlds().get(0).getName() + "").replace("#gameid", ip)
				.replace("#gamemode", Bukkit.getDefaultGameMode().name() + "").replace("#motd", Bukkit.getMotd() + "")
				.replace("#starttime", Bukkit.getIdleTimeout() + "").replace("#status", 1 + "")
				.replace("#memfree", Runtime.getRuntime().freeMemory() + "");
	}

	public ConsoleHandler getConsoleHandler() {
		return consoleHandler;
	}
}