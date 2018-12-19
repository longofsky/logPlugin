package com.sky.logPlugin.dao.entity;

import com.alibaba.fastjson.JSON;

import java.util.Date;


/**
 *
 * @author admin
 * @since 2018-12-12
 */
public class ContentScanDataEntity extends BaseSkyEntity {

	/** 序列化ID */
    private static final long serialVersionUID = 1L;

	/** 注册应用的AppKey **/
	private String appKey;
	/** 请求Id **/
	private String requestId;
	/** 同步异步（1：同步 2：异步） **/
	private Integer syncType;
	/** 内容id **/
	private Long contentId;
	/** 扫描内容 **/
	private String content;
	/** 业务类型（1经纪人房源图片，2二房东房源图片） **/
	private Integer businessType;
	/** 扫描内容类型（1图片2视频3文本等） **/
	private Integer contentType;
	/** 扫描场景（1色情2暴恐3人脸等，多选用英文逗号隔开） **/
	private String scene;
	/** 第三方接口返回任务id（查询时用） **/
	private String taskId;
	/** 重试次数 **/
	private Integer scanTimes;
	/** 扫描结果（1通过2驳回3警告） **/
	private Integer scanResult;
	/** 业务状态(0:待处理1:处理成功2:处理失败) **/
	private Integer status;
	/** 是否删除(0:已删除 1:正常有效) **/
	private Integer valid;
	/** 创建人 **/
	private Long createBy;
	/** 创建人类型 **/
	private Integer createByType;
	/** 创建时间 **/
	private Date createTime;
	/** 修改人 **/
	private Long updateBy;
	/** 修改人类型 **/
	private Integer updateByType;
	/** 修改时间 **/
	private Date updateTime;
	/** 操作流水号 **/
	private Long soDoneCode;
	/** 备注 **/
	private String remark;
	/** 房源数据字段（分散式和经纪人--房源地址，集中式--房型id） **/
	private String roomDesc;

    public String getAppKey() {
       return appKey;
   }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getRequestId() {
       return requestId;
   }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public Integer getSyncType() {
       return syncType;
   }
    public void setSyncType(Integer syncType) {
        this.syncType = syncType;
    }
    public Long getContentId() {
       return contentId;
   }
    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
    public String getContent() {
       return content;
   }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getBusinessType() {
       return businessType;
   }
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }
    public Integer getContentType() {
       return contentType;
   }
    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }
    public String getScene() {
       return scene;
   }
    public void setScene(String scene) {
        this.scene = scene;
    }
    public String getTaskId() {
       return taskId;
   }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public Integer getScanTimes() {
       return scanTimes;
   }
    public void setScanTimes(Integer scanTimes) {
        this.scanTimes = scanTimes;
    }
    public Integer getScanResult() {
       return scanResult;
   }
    public void setScanResult(Integer scanResult) {
        this.scanResult = scanResult;
    }
    public Integer getStatus() {
       return status;
   }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getValid() {
       return valid;
   }
    public void setValid(Integer valid) {
        this.valid = valid;
    }
    public Long getCreateBy() {
       return createBy;
   }
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    public Integer getCreateByType() {
       return createByType;
   }
    public void setCreateByType(Integer createByType) {
        this.createByType = createByType;
    }
    public Date getCreateTime() {
       return createTime;
   }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Long getUpdateBy() {
       return updateBy;
   }
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
    public Integer getUpdateByType() {
       return updateByType;
   }
    public void setUpdateByType(Integer updateByType) {
        this.updateByType = updateByType;
    }
    public Date getUpdateTime() {
       return updateTime;
   }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Long getSoDoneCode() {
       return soDoneCode;
   }
    public void setSoDoneCode(Long soDoneCode) {
        this.soDoneCode = soDoneCode;
    }
    public String getRemark() {
       return remark;
   }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRoomDesc() {
       return roomDesc;
   }
    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}