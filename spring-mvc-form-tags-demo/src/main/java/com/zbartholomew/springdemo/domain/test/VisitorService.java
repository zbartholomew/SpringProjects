package com.zbartholomew.springdemo.domain.test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class VisitorService {

	public VisitorCount updateCount(VisitorCount visitorCount) {
		visitorCount.setCount(visitorCount.getCount() + 1);
		return visitorCount;
	}

	public void registerVisitor(VisitorData sessionData, VisitorData incomingVisitor) {
		List<Visitor> visitors = sessionData.getVisitors();
		sessionData.setCurrentVisitorEmail(incomingVisitor.getCurrentVisitorEmail());
		sessionData.setCurrentVisitorName(incomingVisitor.getCurrentVisitorName());
		visitors.add(new Visitor(incomingVisitor.getCurrentVisitorName(), incomingVisitor.getCurrentVisitorEmail()));
	}

	public long computeDuration(LocalDateTime sessionStartTime) {
		Duration sessionDuration = Duration.between(sessionStartTime, LocalDateTime.now());
		return sessionDuration.getSeconds();
	}

	public String describeCurrentTime(LocalDateTime currentTime) {
		return new StringBuilder().append("Current local time is ").append(currentTime.getHour()).append(":")
				.append(currentTime.getMinute()).append(":").append(currentTime.getSecond()).toString();
	}

	public String describeCurrentDuration(Long currentDuration) {
		long seconds = currentDuration.longValue();
		return new StringBuilder().append("Session duration is ").append(seconds).append(" seconds").toString();
	}
}
