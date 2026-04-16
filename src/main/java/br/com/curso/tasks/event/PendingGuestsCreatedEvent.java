package br.com.curso.tasks.event;

import java.util.List;

public class PendingGuestsCreatedEvent {
    private final Long taskId;
    private final List<Long> pendingGuestIds;

    public PendingGuestsCreatedEvent(Long taskId, List<Long> pendingGuestIds) {
        this.taskId = taskId;
        this.pendingGuestIds = pendingGuestIds;
    }

    public Long getTaskId() {
        return taskId;
    }

    public List<Long> getPendingGuestIds() {
        return pendingGuestIds;
    }
}