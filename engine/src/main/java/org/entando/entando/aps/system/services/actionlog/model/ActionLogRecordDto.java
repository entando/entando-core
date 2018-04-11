package org.entando.entando.aps.system.services.actionlog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamComment;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamLikeInfo;
import org.springframework.beans.BeanUtils;

public class ActionLogRecordDto {

    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    private String username;
    private String namespace;
    private String actionName;
    private String parameters;
    private List<LikeInfo> likes = new ArrayList<>();
    private List<CommentInfo> comments = new ArrayList<>();

    public ActionLogRecordDto() {}

    public ActionLogRecordDto(ActionLogRecord src) {
        this.setActionName(src.getActionName());
        this.setCreatedAt(src.getActionDate());
        this.setId(src.getId());
        this.setNamespace(src.getNamespace());
        this.setParameters(src.getParameters());
        this.setUpdatedAt(src.getUpdateDate());
        this.setUsername(src.getUsername());
    }

    public ActionLogRecordDto(ActionLogRecord src, List<ActivityStreamLikeInfo> actionLikeRecords, List<ActivityStreamComment> actionCommentRecords) {
        this(src);
        if (null != actionLikeRecords) {
            actionLikeRecords.stream().forEach(i -> likes.add(new LikeInfo(i)));
        }
        if (null != actionCommentRecords) {
            actionCommentRecords.stream().forEach(i -> likes.add(new CommentInfo(i)));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public List<LikeInfo> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeInfo> likes) {
        this.likes = likes;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfo> comments) {
        this.comments = comments;
    }

    protected class LikeInfo {

        private String username;
        private String displayName;

        public LikeInfo() {}

        public LikeInfo(ActivityStreamLikeInfo src) {
            BeanUtils.copyProperties(src, this);
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    protected class CommentInfo extends LikeInfo {

        private int id;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private Date commentDate;
        private String commentText;

        public CommentInfo() {

        }

        public CommentInfo(ActivityStreamComment src) {
            BeanUtils.copyProperties(src, this);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Date getCommentDate() {
            return commentDate;
        }

        public void setCommentDate(Date commentDate) {
            this.commentDate = commentDate;
        }

        public String getCommentText() {
            return commentText;
        }

        public void setCommentText(String commentText) {
            this.commentText = commentText;
        }
    }
}
