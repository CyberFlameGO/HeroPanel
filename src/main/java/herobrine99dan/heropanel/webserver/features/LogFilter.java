package herobrine99dan.heropanel.webserver.features;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class LogFilter implements Filter {

	private HeroPanel panel;
	private static final Level minLevel = Level.INFO;

	public LogFilter(HeroPanel panel) {
		this.panel = panel;
	}

	public Filter.Result checkMessage(String message, Level level) {
		if (level.compareTo(minLevel) < 1) {
			this.panel.getConsoleHandler().addLogToSend(message);
		}
		return Filter.Result.NEUTRAL;
	}

	public LifeCycle.State getState() {
		try {
			return LifeCycle.State.STARTED;
		} catch (Exception exception) {
			return null;
		}
	}

	public void initialize() {
	}

	public boolean isStarted() {
		return true;
	}

	public boolean isStopped() {
		return false;
	}

	public void start() {
	}

	public void stop() {
	}

	public Filter.Result filter(LogEvent event) {
		return checkMessage(event.getMessage().getFormattedMessage(), event.getLevel());
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object... arg4) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Object message, Throwable arg4) {
		return checkMessage(message.toString(), arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Message message, Throwable arg4) {
		return checkMessage(message.getFormattedMessage(), arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
			Object arg6) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
			Object arg6, Object arg7) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
			Object arg6, Object arg7, Object arg8) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
			Object arg6, Object arg7, Object arg8, Object arg9) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
			Object arg6, Object arg7, Object arg8, Object arg9, Object arg10) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
			Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
			Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
		return checkMessage(message, arg1);
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object arg4, Object arg5,
			Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12,
			Object arg13) {
		return checkMessage(message, arg1);
	}

	public Filter.Result getOnMatch() {
		return Filter.Result.NEUTRAL;
	}

	public Filter.Result getOnMismatch() {
		return Filter.Result.NEUTRAL;
	}
}
