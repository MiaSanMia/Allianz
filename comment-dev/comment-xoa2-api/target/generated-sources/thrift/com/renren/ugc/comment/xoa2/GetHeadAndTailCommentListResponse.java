/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.renren.ugc.comment.xoa2;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetHeadAndTailCommentListResponse implements org.apache.thrift.TBase<GetHeadAndTailCommentListResponse, GetHeadAndTailCommentListResponse._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("GetHeadAndTailCommentListResponse");

  private static final org.apache.thrift.protocol.TField HEAD_COMMENT_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("headCommentList", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField TAIL_COMMENT_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("tailCommentList", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField BASE_REP_FIELD_DESC = new org.apache.thrift.protocol.TField("baseRep", org.apache.thrift.protocol.TType.STRUCT, (short)3);
  private static final org.apache.thrift.protocol.TField MORE_FIELD_DESC = new org.apache.thrift.protocol.TField("more", org.apache.thrift.protocol.TType.BOOL, (short)4);
  private static final org.apache.thrift.protocol.TField TOTAL_COUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("totalCount", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField ENTRY_FIELD_DESC = new org.apache.thrift.protocol.TField("entry", org.apache.thrift.protocol.TType.STRUCT, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new GetHeadAndTailCommentListResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new GetHeadAndTailCommentListResponseTupleSchemeFactory());
  }

  /**
   * 获取的头部评论的列表
   */
  public List<com.renren.ugc.comment.xoa2.Comment> headCommentList; // optional
  /**
   * 获取的尾部评论的列表
   */
  public List<com.renren.ugc.comment.xoa2.Comment> tailCommentList; // optional
  /**
   * 响应信息
   */
  public com.renren.xoa2.BaseResponse baseRep; // optional
  /**
   * 是否有更多的评论
   */
  public boolean more; // optional
  /**
   * 属于当前Entry的评论的总数
   */
  public long totalCount; // optional
  /**
   * 被评论的“实体”
   */
  public com.renren.ugc.comment.xoa2.Entry entry; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 获取的头部评论的列表
     */
    HEAD_COMMENT_LIST((short)1, "headCommentList"),
    /**
     * 获取的尾部评论的列表
     */
    TAIL_COMMENT_LIST((short)2, "tailCommentList"),
    /**
     * 响应信息
     */
    BASE_REP((short)3, "baseRep"),
    /**
     * 是否有更多的评论
     */
    MORE((short)4, "more"),
    /**
     * 属于当前Entry的评论的总数
     */
    TOTAL_COUNT((short)5, "totalCount"),
    /**
     * 被评论的“实体”
     */
    ENTRY((short)6, "entry");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // HEAD_COMMENT_LIST
          return HEAD_COMMENT_LIST;
        case 2: // TAIL_COMMENT_LIST
          return TAIL_COMMENT_LIST;
        case 3: // BASE_REP
          return BASE_REP;
        case 4: // MORE
          return MORE;
        case 5: // TOTAL_COUNT
          return TOTAL_COUNT;
        case 6: // ENTRY
          return ENTRY;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __MORE_ISSET_ID = 0;
  private static final int __TOTALCOUNT_ISSET_ID = 1;
  private BitSet __isset_bit_vector = new BitSet(2);
  private _Fields optionals[] = {_Fields.HEAD_COMMENT_LIST,_Fields.TAIL_COMMENT_LIST,_Fields.BASE_REP,_Fields.MORE,_Fields.TOTAL_COUNT,_Fields.ENTRY};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.HEAD_COMMENT_LIST, new org.apache.thrift.meta_data.FieldMetaData("headCommentList", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.ugc.comment.xoa2.Comment.class))));
    tmpMap.put(_Fields.TAIL_COMMENT_LIST, new org.apache.thrift.meta_data.FieldMetaData("tailCommentList", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.ugc.comment.xoa2.Comment.class))));
    tmpMap.put(_Fields.BASE_REP, new org.apache.thrift.meta_data.FieldMetaData("baseRep", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.xoa2.BaseResponse.class)));
    tmpMap.put(_Fields.MORE, new org.apache.thrift.meta_data.FieldMetaData("more", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.TOTAL_COUNT, new org.apache.thrift.meta_data.FieldMetaData("totalCount", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ENTRY, new org.apache.thrift.meta_data.FieldMetaData("entry", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.ugc.comment.xoa2.Entry.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(GetHeadAndTailCommentListResponse.class, metaDataMap);
  }

  public GetHeadAndTailCommentListResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public GetHeadAndTailCommentListResponse(GetHeadAndTailCommentListResponse other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetHeadCommentList()) {
      List<com.renren.ugc.comment.xoa2.Comment> __this__headCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>();
      for (com.renren.ugc.comment.xoa2.Comment other_element : other.headCommentList) {
        __this__headCommentList.add(new com.renren.ugc.comment.xoa2.Comment(other_element));
      }
      this.headCommentList = __this__headCommentList;
    }
    if (other.isSetTailCommentList()) {
      List<com.renren.ugc.comment.xoa2.Comment> __this__tailCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>();
      for (com.renren.ugc.comment.xoa2.Comment other_element : other.tailCommentList) {
        __this__tailCommentList.add(new com.renren.ugc.comment.xoa2.Comment(other_element));
      }
      this.tailCommentList = __this__tailCommentList;
    }
    if (other.isSetBaseRep()) {
      this.baseRep = new com.renren.xoa2.BaseResponse(other.baseRep);
    }
    this.more = other.more;
    this.totalCount = other.totalCount;
    if (other.isSetEntry()) {
      this.entry = new com.renren.ugc.comment.xoa2.Entry(other.entry);
    }
  }

  public GetHeadAndTailCommentListResponse deepCopy() {
    return new GetHeadAndTailCommentListResponse(this);
  }

  @Override
  public void clear() {
    this.headCommentList = null;
    this.tailCommentList = null;
    this.baseRep = null;
    setMoreIsSet(false);
    this.more = false;
    setTotalCountIsSet(false);
    this.totalCount = 0;
    this.entry = null;
  }

  public int getHeadCommentListSize() {
    return (this.headCommentList == null) ? 0 : this.headCommentList.size();
  }

  public java.util.Iterator<com.renren.ugc.comment.xoa2.Comment> getHeadCommentListIterator() {
    return (this.headCommentList == null) ? null : this.headCommentList.iterator();
  }

  public void addToHeadCommentList(com.renren.ugc.comment.xoa2.Comment elem) {
    if (this.headCommentList == null) {
      this.headCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>();
    }
    this.headCommentList.add(elem);
  }

  /**
   * 获取的头部评论的列表
   */
  public List<com.renren.ugc.comment.xoa2.Comment> getHeadCommentList() {
    return this.headCommentList;
  }

  /**
   * 获取的头部评论的列表
   */
  public GetHeadAndTailCommentListResponse setHeadCommentList(List<com.renren.ugc.comment.xoa2.Comment> headCommentList) {
    this.headCommentList = headCommentList;
    return this;
  }

  public void unsetHeadCommentList() {
    this.headCommentList = null;
  }

  /** Returns true if field headCommentList is set (has been assigned a value) and false otherwise */
  public boolean isSetHeadCommentList() {
    return this.headCommentList != null;
  }

  public void setHeadCommentListIsSet(boolean value) {
    if (!value) {
      this.headCommentList = null;
    }
  }

  public int getTailCommentListSize() {
    return (this.tailCommentList == null) ? 0 : this.tailCommentList.size();
  }

  public java.util.Iterator<com.renren.ugc.comment.xoa2.Comment> getTailCommentListIterator() {
    return (this.tailCommentList == null) ? null : this.tailCommentList.iterator();
  }

  public void addToTailCommentList(com.renren.ugc.comment.xoa2.Comment elem) {
    if (this.tailCommentList == null) {
      this.tailCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>();
    }
    this.tailCommentList.add(elem);
  }

  /**
   * 获取的尾部评论的列表
   */
  public List<com.renren.ugc.comment.xoa2.Comment> getTailCommentList() {
    return this.tailCommentList;
  }

  /**
   * 获取的尾部评论的列表
   */
  public GetHeadAndTailCommentListResponse setTailCommentList(List<com.renren.ugc.comment.xoa2.Comment> tailCommentList) {
    this.tailCommentList = tailCommentList;
    return this;
  }

  public void unsetTailCommentList() {
    this.tailCommentList = null;
  }

  /** Returns true if field tailCommentList is set (has been assigned a value) and false otherwise */
  public boolean isSetTailCommentList() {
    return this.tailCommentList != null;
  }

  public void setTailCommentListIsSet(boolean value) {
    if (!value) {
      this.tailCommentList = null;
    }
  }

  /**
   * 响应信息
   */
  public com.renren.xoa2.BaseResponse getBaseRep() {
    return this.baseRep;
  }

  /**
   * 响应信息
   */
  public GetHeadAndTailCommentListResponse setBaseRep(com.renren.xoa2.BaseResponse baseRep) {
    this.baseRep = baseRep;
    return this;
  }

  public void unsetBaseRep() {
    this.baseRep = null;
  }

  /** Returns true if field baseRep is set (has been assigned a value) and false otherwise */
  public boolean isSetBaseRep() {
    return this.baseRep != null;
  }

  public void setBaseRepIsSet(boolean value) {
    if (!value) {
      this.baseRep = null;
    }
  }

  /**
   * 是否有更多的评论
   */
  public boolean isMore() {
    return this.more;
  }

  /**
   * 是否有更多的评论
   */
  public GetHeadAndTailCommentListResponse setMore(boolean more) {
    this.more = more;
    setMoreIsSet(true);
    return this;
  }

  public void unsetMore() {
    __isset_bit_vector.clear(__MORE_ISSET_ID);
  }

  /** Returns true if field more is set (has been assigned a value) and false otherwise */
  public boolean isSetMore() {
    return __isset_bit_vector.get(__MORE_ISSET_ID);
  }

  public void setMoreIsSet(boolean value) {
    __isset_bit_vector.set(__MORE_ISSET_ID, value);
  }

  /**
   * 属于当前Entry的评论的总数
   */
  public long getTotalCount() {
    return this.totalCount;
  }

  /**
   * 属于当前Entry的评论的总数
   */
  public GetHeadAndTailCommentListResponse setTotalCount(long totalCount) {
    this.totalCount = totalCount;
    setTotalCountIsSet(true);
    return this;
  }

  public void unsetTotalCount() {
    __isset_bit_vector.clear(__TOTALCOUNT_ISSET_ID);
  }

  /** Returns true if field totalCount is set (has been assigned a value) and false otherwise */
  public boolean isSetTotalCount() {
    return __isset_bit_vector.get(__TOTALCOUNT_ISSET_ID);
  }

  public void setTotalCountIsSet(boolean value) {
    __isset_bit_vector.set(__TOTALCOUNT_ISSET_ID, value);
  }

  /**
   * 被评论的“实体”
   */
  public com.renren.ugc.comment.xoa2.Entry getEntry() {
    return this.entry;
  }

  /**
   * 被评论的“实体”
   */
  public GetHeadAndTailCommentListResponse setEntry(com.renren.ugc.comment.xoa2.Entry entry) {
    this.entry = entry;
    return this;
  }

  public void unsetEntry() {
    this.entry = null;
  }

  /** Returns true if field entry is set (has been assigned a value) and false otherwise */
  public boolean isSetEntry() {
    return this.entry != null;
  }

  public void setEntryIsSet(boolean value) {
    if (!value) {
      this.entry = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case HEAD_COMMENT_LIST:
      if (value == null) {
        unsetHeadCommentList();
      } else {
        setHeadCommentList((List<com.renren.ugc.comment.xoa2.Comment>)value);
      }
      break;

    case TAIL_COMMENT_LIST:
      if (value == null) {
        unsetTailCommentList();
      } else {
        setTailCommentList((List<com.renren.ugc.comment.xoa2.Comment>)value);
      }
      break;

    case BASE_REP:
      if (value == null) {
        unsetBaseRep();
      } else {
        setBaseRep((com.renren.xoa2.BaseResponse)value);
      }
      break;

    case MORE:
      if (value == null) {
        unsetMore();
      } else {
        setMore((Boolean)value);
      }
      break;

    case TOTAL_COUNT:
      if (value == null) {
        unsetTotalCount();
      } else {
        setTotalCount((Long)value);
      }
      break;

    case ENTRY:
      if (value == null) {
        unsetEntry();
      } else {
        setEntry((com.renren.ugc.comment.xoa2.Entry)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case HEAD_COMMENT_LIST:
      return getHeadCommentList();

    case TAIL_COMMENT_LIST:
      return getTailCommentList();

    case BASE_REP:
      return getBaseRep();

    case MORE:
      return Boolean.valueOf(isMore());

    case TOTAL_COUNT:
      return Long.valueOf(getTotalCount());

    case ENTRY:
      return getEntry();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case HEAD_COMMENT_LIST:
      return isSetHeadCommentList();
    case TAIL_COMMENT_LIST:
      return isSetTailCommentList();
    case BASE_REP:
      return isSetBaseRep();
    case MORE:
      return isSetMore();
    case TOTAL_COUNT:
      return isSetTotalCount();
    case ENTRY:
      return isSetEntry();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof GetHeadAndTailCommentListResponse)
      return this.equals((GetHeadAndTailCommentListResponse)that);
    return false;
  }

  public boolean equals(GetHeadAndTailCommentListResponse that) {
    if (that == null)
      return false;

    boolean this_present_headCommentList = true && this.isSetHeadCommentList();
    boolean that_present_headCommentList = true && that.isSetHeadCommentList();
    if (this_present_headCommentList || that_present_headCommentList) {
      if (!(this_present_headCommentList && that_present_headCommentList))
        return false;
      if (!this.headCommentList.equals(that.headCommentList))
        return false;
    }

    boolean this_present_tailCommentList = true && this.isSetTailCommentList();
    boolean that_present_tailCommentList = true && that.isSetTailCommentList();
    if (this_present_tailCommentList || that_present_tailCommentList) {
      if (!(this_present_tailCommentList && that_present_tailCommentList))
        return false;
      if (!this.tailCommentList.equals(that.tailCommentList))
        return false;
    }

    boolean this_present_baseRep = true && this.isSetBaseRep();
    boolean that_present_baseRep = true && that.isSetBaseRep();
    if (this_present_baseRep || that_present_baseRep) {
      if (!(this_present_baseRep && that_present_baseRep))
        return false;
      if (!this.baseRep.equals(that.baseRep))
        return false;
    }

    boolean this_present_more = true && this.isSetMore();
    boolean that_present_more = true && that.isSetMore();
    if (this_present_more || that_present_more) {
      if (!(this_present_more && that_present_more))
        return false;
      if (this.more != that.more)
        return false;
    }

    boolean this_present_totalCount = true && this.isSetTotalCount();
    boolean that_present_totalCount = true && that.isSetTotalCount();
    if (this_present_totalCount || that_present_totalCount) {
      if (!(this_present_totalCount && that_present_totalCount))
        return false;
      if (this.totalCount != that.totalCount)
        return false;
    }

    boolean this_present_entry = true && this.isSetEntry();
    boolean that_present_entry = true && that.isSetEntry();
    if (this_present_entry || that_present_entry) {
      if (!(this_present_entry && that_present_entry))
        return false;
      if (!this.entry.equals(that.entry))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_headCommentList = true && (isSetHeadCommentList());
    builder.append(present_headCommentList);
    if (present_headCommentList)
      builder.append(headCommentList);

    boolean present_tailCommentList = true && (isSetTailCommentList());
    builder.append(present_tailCommentList);
    if (present_tailCommentList)
      builder.append(tailCommentList);

    boolean present_baseRep = true && (isSetBaseRep());
    builder.append(present_baseRep);
    if (present_baseRep)
      builder.append(baseRep);

    boolean present_more = true && (isSetMore());
    builder.append(present_more);
    if (present_more)
      builder.append(more);

    boolean present_totalCount = true && (isSetTotalCount());
    builder.append(present_totalCount);
    if (present_totalCount)
      builder.append(totalCount);

    boolean present_entry = true && (isSetEntry());
    builder.append(present_entry);
    if (present_entry)
      builder.append(entry);

    return builder.toHashCode();
  }

  public int compareTo(GetHeadAndTailCommentListResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    GetHeadAndTailCommentListResponse typedOther = (GetHeadAndTailCommentListResponse)other;

    lastComparison = Boolean.valueOf(isSetHeadCommentList()).compareTo(typedOther.isSetHeadCommentList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHeadCommentList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.headCommentList, typedOther.headCommentList);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTailCommentList()).compareTo(typedOther.isSetTailCommentList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTailCommentList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tailCommentList, typedOther.tailCommentList);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBaseRep()).compareTo(typedOther.isSetBaseRep());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBaseRep()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.baseRep, typedOther.baseRep);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMore()).compareTo(typedOther.isSetMore());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMore()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.more, typedOther.more);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTotalCount()).compareTo(typedOther.isSetTotalCount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTotalCount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.totalCount, typedOther.totalCount);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEntry()).compareTo(typedOther.isSetEntry());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEntry()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.entry, typedOther.entry);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("GetHeadAndTailCommentListResponse(");
    boolean first = true;

    if (isSetHeadCommentList()) {
      sb.append("headCommentList:");
      if (this.headCommentList == null) {
        sb.append("null");
      } else {
        sb.append(this.headCommentList);
      }
      first = false;
    }
    if (isSetTailCommentList()) {
      if (!first) sb.append(", ");
      sb.append("tailCommentList:");
      if (this.tailCommentList == null) {
        sb.append("null");
      } else {
        sb.append(this.tailCommentList);
      }
      first = false;
    }
    if (isSetBaseRep()) {
      if (!first) sb.append(", ");
      sb.append("baseRep:");
      if (this.baseRep == null) {
        sb.append("null");
      } else {
        sb.append(this.baseRep);
      }
      first = false;
    }
    if (isSetMore()) {
      if (!first) sb.append(", ");
      sb.append("more:");
      sb.append(this.more);
      first = false;
    }
    if (isSetTotalCount()) {
      if (!first) sb.append(", ");
      sb.append("totalCount:");
      sb.append(this.totalCount);
      first = false;
    }
    if (isSetEntry()) {
      if (!first) sb.append(", ");
      sb.append("entry:");
      if (this.entry == null) {
        sb.append("null");
      } else {
        sb.append(this.entry);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class GetHeadAndTailCommentListResponseStandardSchemeFactory implements SchemeFactory {
    public GetHeadAndTailCommentListResponseStandardScheme getScheme() {
      return new GetHeadAndTailCommentListResponseStandardScheme();
    }
  }

  private static class GetHeadAndTailCommentListResponseStandardScheme extends StandardScheme<GetHeadAndTailCommentListResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, GetHeadAndTailCommentListResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // HEAD_COMMENT_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list200 = iprot.readListBegin();
                struct.headCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>(_list200.size);
                for (int _i201 = 0; _i201 < _list200.size; ++_i201)
                {
                  com.renren.ugc.comment.xoa2.Comment _elem202; // required
                  _elem202 = new com.renren.ugc.comment.xoa2.Comment();
                  _elem202.read(iprot);
                  struct.headCommentList.add(_elem202);
                }
                iprot.readListEnd();
              }
              struct.setHeadCommentListIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TAIL_COMMENT_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list203 = iprot.readListBegin();
                struct.tailCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>(_list203.size);
                for (int _i204 = 0; _i204 < _list203.size; ++_i204)
                {
                  com.renren.ugc.comment.xoa2.Comment _elem205; // required
                  _elem205 = new com.renren.ugc.comment.xoa2.Comment();
                  _elem205.read(iprot);
                  struct.tailCommentList.add(_elem205);
                }
                iprot.readListEnd();
              }
              struct.setTailCommentListIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // BASE_REP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.baseRep = new com.renren.xoa2.BaseResponse();
              struct.baseRep.read(iprot);
              struct.setBaseRepIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // MORE
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.more = iprot.readBool();
              struct.setMoreIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // TOTAL_COUNT
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.totalCount = iprot.readI64();
              struct.setTotalCountIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // ENTRY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.entry = new com.renren.ugc.comment.xoa2.Entry();
              struct.entry.read(iprot);
              struct.setEntryIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, GetHeadAndTailCommentListResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.headCommentList != null) {
        if (struct.isSetHeadCommentList()) {
          oprot.writeFieldBegin(HEAD_COMMENT_LIST_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.headCommentList.size()));
            for (com.renren.ugc.comment.xoa2.Comment _iter206 : struct.headCommentList)
            {
              _iter206.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.tailCommentList != null) {
        if (struct.isSetTailCommentList()) {
          oprot.writeFieldBegin(TAIL_COMMENT_LIST_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.tailCommentList.size()));
            for (com.renren.ugc.comment.xoa2.Comment _iter207 : struct.tailCommentList)
            {
              _iter207.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.baseRep != null) {
        if (struct.isSetBaseRep()) {
          oprot.writeFieldBegin(BASE_REP_FIELD_DESC);
          struct.baseRep.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetMore()) {
        oprot.writeFieldBegin(MORE_FIELD_DESC);
        oprot.writeBool(struct.more);
        oprot.writeFieldEnd();
      }
      if (struct.isSetTotalCount()) {
        oprot.writeFieldBegin(TOTAL_COUNT_FIELD_DESC);
        oprot.writeI64(struct.totalCount);
        oprot.writeFieldEnd();
      }
      if (struct.entry != null) {
        if (struct.isSetEntry()) {
          oprot.writeFieldBegin(ENTRY_FIELD_DESC);
          struct.entry.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class GetHeadAndTailCommentListResponseTupleSchemeFactory implements SchemeFactory {
    public GetHeadAndTailCommentListResponseTupleScheme getScheme() {
      return new GetHeadAndTailCommentListResponseTupleScheme();
    }
  }

  private static class GetHeadAndTailCommentListResponseTupleScheme extends TupleScheme<GetHeadAndTailCommentListResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, GetHeadAndTailCommentListResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetHeadCommentList()) {
        optionals.set(0);
      }
      if (struct.isSetTailCommentList()) {
        optionals.set(1);
      }
      if (struct.isSetBaseRep()) {
        optionals.set(2);
      }
      if (struct.isSetMore()) {
        optionals.set(3);
      }
      if (struct.isSetTotalCount()) {
        optionals.set(4);
      }
      if (struct.isSetEntry()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetHeadCommentList()) {
        {
          oprot.writeI32(struct.headCommentList.size());
          for (com.renren.ugc.comment.xoa2.Comment _iter208 : struct.headCommentList)
          {
            _iter208.write(oprot);
          }
        }
      }
      if (struct.isSetTailCommentList()) {
        {
          oprot.writeI32(struct.tailCommentList.size());
          for (com.renren.ugc.comment.xoa2.Comment _iter209 : struct.tailCommentList)
          {
            _iter209.write(oprot);
          }
        }
      }
      if (struct.isSetBaseRep()) {
        struct.baseRep.write(oprot);
      }
      if (struct.isSetMore()) {
        oprot.writeBool(struct.more);
      }
      if (struct.isSetTotalCount()) {
        oprot.writeI64(struct.totalCount);
      }
      if (struct.isSetEntry()) {
        struct.entry.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, GetHeadAndTailCommentListResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list210 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.headCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>(_list210.size);
          for (int _i211 = 0; _i211 < _list210.size; ++_i211)
          {
            com.renren.ugc.comment.xoa2.Comment _elem212; // required
            _elem212 = new com.renren.ugc.comment.xoa2.Comment();
            _elem212.read(iprot);
            struct.headCommentList.add(_elem212);
          }
        }
        struct.setHeadCommentListIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list213 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.tailCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>(_list213.size);
          for (int _i214 = 0; _i214 < _list213.size; ++_i214)
          {
            com.renren.ugc.comment.xoa2.Comment _elem215; // required
            _elem215 = new com.renren.ugc.comment.xoa2.Comment();
            _elem215.read(iprot);
            struct.tailCommentList.add(_elem215);
          }
        }
        struct.setTailCommentListIsSet(true);
      }
      if (incoming.get(2)) {
        struct.baseRep = new com.renren.xoa2.BaseResponse();
        struct.baseRep.read(iprot);
        struct.setBaseRepIsSet(true);
      }
      if (incoming.get(3)) {
        struct.more = iprot.readBool();
        struct.setMoreIsSet(true);
      }
      if (incoming.get(4)) {
        struct.totalCount = iprot.readI64();
        struct.setTotalCountIsSet(true);
      }
      if (incoming.get(5)) {
        struct.entry = new com.renren.ugc.comment.xoa2.Entry();
        struct.entry.read(iprot);
        struct.setEntryIsSet(true);
      }
    }
  }

}
