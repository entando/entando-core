package org.entando.entando.aps.system.services.actionlog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamComment;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamLikeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class ActionLogRecordDto {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    private String username;
    private String namespace;
    private String actionName;
    private Map<String, String> parameters = new HashMap<>();
    private List<LikeInfo> likes = new ArrayList<>();
    private List<CommentInfo> comments = new ArrayList<>();

    public ActionLogRecordDto() {
    }

    public ActionLogRecordDto(ActionLogRecord src) {
        this.setActionName(src.getActionName());
        this.setCreatedAt(src.getActionDate());
        this.setId(src.getId());
        this.setNamespace(src.getNamespace());
        if (!StringUtils.isEmpty(src.getParameters())) {
            List<String> lines = this.readLines(src.getParameters());
            for (String line : lines) {
                String[] sections = line.split("=");
                if (sections.length == 2) {
                    this.getParameters().put(sections[0], sections[1]);
                }
            }
        }
        this.setUpdatedAt(src.getUpdateDate());
        this.setUsername(src.getUsername());
    }

    private List<String> readLines(String text) {
        InputStream is = null;
        List<String> lines = new ArrayList<>();
        try {
            is = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                lines.add(strLine);
            }
        } catch (Throwable t) {
            logger.error("Error reading lines", t);
            throw new RuntimeException("Error reading lines", t);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new RuntimeException("Error cloasing stream", ex);
                }
            }
        }
        return lines;
    }

    public ActionLogRecordDto(ActionLogRecord src, List<ActivityStreamLikeInfo> actionLikeRecords, List<ActivityStreamComment> actionCommentRecords) {
        this(src);
        if (null != actionLikeRecords) {
            actionLikeRecords.stream().forEach(i -> likes.add(new LikeInfo(i)));
        }
        if (null != actionCommentRecords) {
            actionCommentRecords.stream().forEach(i -> comments.add(new CommentInfo(i)));
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

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
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

    public static String getEntityFieldName(String dtoFieldName) {
        switch (dtoFieldName) {
            case "createdAt":
                return "actiondate";
            case "updatedAt":
                return "updatedate";
            default:
                return dtoFieldName;
        }
    }

    protected class LikeInfo {

        private String username;
        private String displayName;

        public LikeInfo() {
        }

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
