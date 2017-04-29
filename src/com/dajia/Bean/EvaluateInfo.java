package com.dajia.Bean;

public class EvaluateInfo
{
  private String comments;
  private String employee_id;
  private String insert_time;
  private String level;
  private String name;
  private String status;
  private String uuid;

  public int describeContents()
  {
    return 0;
  }

  public String getComments()
  {
    return this.comments;
  }

  public String getEmployee_id()
  {
    return this.employee_id;
  }

  public String getInsert_time()
  {
    return this.insert_time;
  }

  public String getLevel()
  {
    return this.level;
  }

  public String getName()
  {
    return this.name;
  }

  public String getStatus()
  {
    return this.status;
  }

  public String getUuid()
  {
    return this.uuid;
  }

  public void setComments(String paramString)
  {
    this.comments = paramString;
  }

  public void setEmployee_id(String paramString)
  {
    this.employee_id = paramString;
  }

  public void setInsert_time(String paramString)
  {
    this.insert_time = paramString;
  }

  public void setLevel(String paramString)
  {
    this.level = paramString;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setStatus(String paramString)
  {
    this.status = paramString;
  }

  public void setUuid(String paramString)
  {
    this.uuid = paramString;
  }

  public String toString()
  {
    return new StringBuilder().append(" | ").append(this.level).toString() + new StringBuilder().append(" | ").append(this.employee_id).toString() + new StringBuilder().append(" | ").append(this.insert_time).toString() + new StringBuilder().append(" | ").append(this.name).toString() + new StringBuilder().append(" | ").append(this.status).toString() + new StringBuilder().append(" | ").append(this.uuid).toString() + new StringBuilder().append(" | ").append(this.comments).toString();
  }

}