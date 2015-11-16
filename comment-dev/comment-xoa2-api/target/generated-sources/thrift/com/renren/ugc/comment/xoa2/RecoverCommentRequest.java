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

public class RecoverCommentRequest implements org.apache.thrift.TBase<RecoverCommentRequest, RecoverCommentRequest._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RecoverCommentRequest");

  private static final org.apache.thrift.protocol.TField TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("type", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField ACTOR_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("actorId", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField ENTRY_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("entryId", org.apache.thrift.protocol.TType.I64, (short)3);
  private static final org.apache.thrift.protocol.TField ENTRY_OWNER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("entryOwnerId", org.apache.thrift.protocol.TType.I32, (short)4);
  private static final org.apache.thrift.protocol.TField COMMENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("commentId", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField PARAMS_FIELD_DESC = new org.apache.thrift.protocol.TField("params", org.apache.thrift.protocol.TType.MAP, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RecoverCommentRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RecoverCommentRequestTupleSchemeFactory());
  }

  /**
   * 评论业务类型
   * 
   * @see com.renren.ugc.comment.xoa2.CommentType
   */
  public com.renren.ugc.comment.xoa2.CommentType type; // required
  /**
   * 恢复评论者用户id, 这个是必须的参数，
   * 但是为了保证兼容性标记为optional
   */
  public int actorId; // optional
  /**
   * 被评论实体Id
   */
  public long entryId; // required
  /**
   * 被评论实体所有者Id
   */
  public int entryOwnerId; // required
  /**
   * 要恢复的评论Id
   */
  public long commentId; // required
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  public Map<String,String> params; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 评论业务类型
     * 
     * @see com.renren.ugc.comment.xoa2.CommentType
     */
    TYPE((short)1, "type"),
    /**
     * 恢复评论者用户id, 这个是必须的参数，
     * 但是为了保证兼容性标记为optional
     */
    ACTOR_ID((short)2, "actorId"),
    /**
     * 被评论实体Id
     */
    ENTRY_ID((short)3, "entryId"),
    /**
     * 被评论实体所有者Id
     */
    ENTRY_OWNER_ID((short)4, "entryOwnerId"),
    /**
     * 要恢复的评论Id
     */
    COMMENT_ID((short)5, "commentId"),
    /**
     * 额外的参数，用于传入业务相关的参数
     */
    PARAMS((short)6, "params");

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
        case 1: // TYPE
          return TYPE;
        case 2: // ACTOR_ID
          return ACTOR_ID;
        case 3: // ENTRY_ID
          return ENTRY_ID;
        case 4: // ENTRY_OWNER_ID
          return ENTRY_OWNER_ID;
        case 5: // COMMENT_ID
          return COMMENT_ID;
        case 6: // PARAMS
          return PARAMS;
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
  private static final int __ACTORID_ISSET_ID = 0;
  private static final int __ENTRYID_ISSET_ID = 1;
  private static final int __ENTRYOWNERID_ISSET_ID = 2;
  private static final int __COMMENTID_ISSET_ID = 3;
  private BitSet __isset_bit_vector = new BitSet(4);
  private _Fields optionals[] = {_Fields.ACTOR_ID,_Fields.PARAMS};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TYPE, new org.apache.thrift.meta_data.FieldMetaData("type", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, com.renren.ugc.comment.xoa2.CommentType.class)));
    tmpMap.put(_Fields.ACTOR_ID, new org.apache.thrift.meta_data.FieldMetaData("actorId", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.ENTRY_ID, new org.apache.thrift.meta_data.FieldMetaData("entryId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ENTRY_OWNER_ID, new org.apache.thrift.meta_data.FieldMetaData("entryOwnerId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.COMMENT_ID, new org.apache.thrift.meta_data.FieldMetaData("commentId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.PARAMS, new org.apache.thrift.meta_data.FieldMetaData("params", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RecoverCommentRequest.class, metaDataMap);
  }

  public RecoverCommentRequest() {
  }

  public RecoverCommentRequest(
    com.renren.ugc.comment.xoa2.CommentType type,
    long entryId,
    int entryOwnerId,
    long commentId)
  {
    this();
    this.type = type;
    this.entryId = entryId;
    setEntryIdIsSet(true);
    this.entryOwnerId = entryOwnerId;
    setEntryOwnerIdIsSet(true);
    this.commentId = commentId;
    setCommentIdIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RecoverCommentRequest(RecoverCommentRequest other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetType()) {
      this.type = other.type;
    }
    this.actorId = other.actorId;
    this.entryId = other.entryId;
    this.entryOwnerId = other.entryOwnerId;
    this.commentId = other.commentId;
    if (other.isSetParams()) {
      Map<String,String> __this__params = new HashMap<String,String>();
      for (Map.Entry<String, String> other_element : other.params.entrySet()) {

        String other_element_key = other_element.getKey();
        String other_element_value = other_element.getValue();

        String __this__params_copy_key = other_element_key;

        String __this__params_copy_value = other_element_value;

        __this__params.put(__this__params_copy_key, __this__params_copy_value);
      }
      this.params = __this__params;
    }
  }

  public RecoverCommentRequest deepCopy() {
    return new RecoverCommentRequest(this);
  }

  @Override
  public void clear() {
    this.type = null;
    setActorIdIsSet(false);
    this.actorId = 0;
    setEntryIdIsSet(false);
    this.entryId = 0;
    setEntryOwnerIdIsSet(false);
    this.entryOwnerId = 0;
    setCommentIdIsSet(false);
    this.commentId = 0;
    this.params = null;
  }

  /**
   * 评论业务类型
   * 
   * @see com.renren.ugc.comment.xoa2.CommentType
   */
  public com.renren.ugc.comment.xoa2.CommentType getType() {
    return this.type;
  }

  /**
   * 评论业务类型
   * 
   * @see com.renren.ugc.comment.xoa2.CommentType
   */
  public RecoverCommentRequest setType(com.renren.ugc.comment.xoa2.CommentType type) {
    this.type = type;
    return this;
  }

  public void unsetType() {
    this.type = null;
  }

  /** Returns true if field type is set (has been assigned a value) and false otherwise */
  public boolean isSetType() {
    return this.type != null;
  }

  public void setTypeIsSet(boolean value) {
    if (!value) {
      this.type = null;
    }
  }

  /**
   * 恢复评论者用户id, 这个是必须的参数，
   * 但是为了保证兼容性标记为optional
   */
  public int getActorId() {
    return this.actorId;
  }

  /**
   * 恢复评论者用户id, 这个是必须的参数，
   * 但是为了保证兼容性标记为optional
   */
  public RecoverCommentRequest setActorId(int actorId) {
    this.actorId = actorId;
    setActorIdIsSet(true);
    return this;
  }

  public void unsetActorId() {
    __isset_bit_vector.clear(__ACTORID_ISSET_ID);
  }

  /** Returns true if field actorId is set (has been assigned a value) and false otherwise */
  public boolean isSetActorId() {
    return __isset_bit_vector.get(__ACTORID_ISSET_ID);
  }

  public void setActorIdIsSet(boolean value) {
    __isset_bit_vector.set(__ACTORID_ISSET_ID, value);
  }

  /**
   * 被评论实体Id
   */
  public long getEntryId() {
    return this.entryId;
  }

  /**
   * 被评论实体Id
   */
  public RecoverCommentRequest setEntryId(long entryId) {
    this.entryId = entryId;
    setEntryIdIsSet(true);
    return this;
  }

  public void unsetEntryId() {
    __isset_bit_vector.clear(__ENTRYID_ISSET_ID);
  }

  /** Returns true if field entryId is set (has been assigned a value) and false otherwise */
  public boolean isSetEntryId() {
    return __isset_bit_vector.get(__ENTRYID_ISSET_ID);
  }

  public void setEntryIdIsSet(boolean value) {
    __isset_bit_vector.set(__ENTRYID_ISSET_ID, value);
  }

  /**
   * 被评论实体所有者Id
   */
  public int getEntryOwnerId() {
    return this.entryOwnerId;
  }

  /**
   * 被评论实体所有者Id
   */
  public RecoverCommentRequest setEntryOwnerId(int entryOwnerId) {
    this.entryOwnerId = entryOwnerId;
    setEntryOwnerIdIsSet(true);
    return this;
  }

  public void unsetEntryOwnerId() {
    __isset_bit_vector.clear(__ENTRYOWNERID_ISSET_ID);
  }

  /** Returns true if field entryOwnerId is set (has been assigned a value) and false otherwise */
  public boolean isSetEntryOwnerId() {
    return __isset_bit_vector.get(__ENTRYOWNERID_ISSET_ID);
  }

  public void setEntryOwnerIdIsSet(boolean value) {
    __isset_bit_vector.set(__ENTRYOWNERID_ISSET_ID, value);
  }

  /**
   * 要恢复的评论Id
   */
  public long getCommentId() {
    return this.commentId;
  }

  /**
   * 要恢复的评论Id
   */
  public RecoverCommentRequest setCommentId(long commentId) {
    this.commentId = commentId;
    setCommentIdIsSet(true);
    return this;
  }

  public void unsetCommentId() {
    __isset_bit_vector.clear(__COMMENTID_ISSET_ID);
  }

  /** Returns true if field commentId is set (has been assigned a value) and false otherwise */
  public boolean isSetCommentId() {
    return __isset_bit_vector.get(__COMMENTID_ISSET_ID);
  }

  public void setCommentIdIsSet(boolean value) {
    __isset_bit_vector.set(__COMMENTID_ISSET_ID, value);
  }

  public int getParamsSize() {
    return (this.params == null) ? 0 : this.params.size();
  }

  public void putToParams(String key, String val) {
    if (this.params == null) {
      this.params = new HashMap<String,String>();
    }
    this.params.put(key, val);
  }

  /**
   * 额外的参数，用于传入业务相关的参数
   */
  public Map<String,String> getParams() {
    return this.params;
  }

  /**
   * 额外的参数，用于传入业务相关的参数
   */
  public RecoverCommentRequest setParams(Map<String,String> params) {
    this.params = params;
    return this;
  }

  public void unsetParams() {
    this.params = null;
  }

  /** Returns true if field params is set (has been assigned a value) and false otherwise */
  public boolean isSetParams() {
    return this.params != null;
  }

  public void setParamsIsSet(boolean value) {
    if (!value) {
      this.params = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TYPE:
      if (value == null) {
        unsetType();
      } else {
        setType((com.renren.ugc.comment.xoa2.CommentType)value);
      }
      break;

    case ACTOR_ID:
      if (value == null) {
        unsetActorId();
      } else {
        setActorId((Integer)value);
      }
      break;

    case ENTRY_ID:
      if (value == null) {
        unsetEntryId();
      } else {
        setEntryId((Long)value);
      }
      break;

    case ENTRY_OWNER_ID:
      if (value == null) {
        unsetEntryOwnerId();
      } else {
        setEntryOwnerId((Integer)value);
      }
      break;

    case COMMENT_ID:
      if (value == null) {
        unsetCommentId();
      } else {
        setCommentId((Long)value);
      }
      break;

    case PARAMS:
      if (value == null) {
        unsetParams();
      } else {
        setParams((Map<String,String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TYPE:
      return getType();

    case ACTOR_ID:
      return Integer.valueOf(getActorId());

    case ENTRY_ID:
      return Long.valueOf(getEntryId());

    case ENTRY_OWNER_ID:
      return Integer.valueOf(getEntryOwnerId());

    case COMMENT_ID:
      return Long.valueOf(getCommentId());

    case PARAMS:
      return getParams();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TYPE:
      return isSetType();
    case ACTOR_ID:
      return isSetActorId();
    case ENTRY_ID:
      return isSetEntryId();
    case ENTRY_OWNER_ID:
      return isSetEntryOwnerId();
    case COMMENT_ID:
      return isSetCommentId();
    case PARAMS:
      return isSetParams();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof RecoverCommentRequest)
      return this.equals((RecoverCommentRequest)that);
    return false;
  }

  public boolean equals(RecoverCommentRequest that) {
    if (that == null)
      return false;

    boolean this_present_type = true && this.isSetType();
    boolean that_present_type = true && that.isSetType();
    if (this_present_type || that_present_type) {
      if (!(this_present_type && that_present_type))
        return false;
      if (!this.type.equals(that.type))
        return false;
    }

    boolean this_present_actorId = true && this.isSetActorId();
    boolean that_present_actorId = true && that.isSetActorId();
    if (this_present_actorId || that_present_actorId) {
      if (!(this_present_actorId && that_present_actorId))
        return false;
      if (this.actorId != that.actorId)
        return false;
    }

    boolean this_present_entryId = true;
    boolean that_present_entryId = true;
    if (this_present_entryId || that_present_entryId) {
      if (!(this_present_entryId && that_present_entryId))
        return false;
      if (this.entryId != that.entryId)
        return false;
    }

    boolean this_present_entryOwnerId = true;
    boolean that_present_entryOwnerId = true;
    if (this_present_entryOwnerId || that_present_entryOwnerId) {
      if (!(this_present_entryOwnerId && that_present_entryOwnerId))
        return false;
      if (this.entryOwnerId != that.entryOwnerId)
        return false;
    }

    boolean this_present_commentId = true;
    boolean that_present_commentId = true;
    if (this_present_commentId || that_present_commentId) {
      if (!(this_present_commentId && that_present_commentId))
        return false;
      if (this.commentId != that.commentId)
        return false;
    }

    boolean this_present_params = true && this.isSetParams();
    boolean that_present_params = true && that.isSetParams();
    if (this_present_params || that_present_params) {
      if (!(this_present_params && that_present_params))
        return false;
      if (!this.params.equals(that.params))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_type = true && (isSetType());
    builder.append(present_type);
    if (present_type)
      builder.append(type.getValue());

    boolean present_actorId = true && (isSetActorId());
    builder.append(present_actorId);
    if (present_actorId)
      builder.append(actorId);

    boolean present_entryId = true;
    builder.append(present_entryId);
    if (present_entryId)
      builder.append(entryId);

    boolean present_entryOwnerId = true;
    builder.append(present_entryOwnerId);
    if (present_entryOwnerId)
      builder.append(entryOwnerId);

    boolean present_commentId = true;
    builder.append(present_commentId);
    if (present_commentId)
      builder.append(commentId);

    boolean present_params = true && (isSetParams());
    builder.append(present_params);
    if (present_params)
      builder.append(params);

    return builder.toHashCode();
  }

  public int compareTo(RecoverCommentRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    RecoverCommentRequest typedOther = (RecoverCommentRequest)other;

    lastComparison = Boolean.valueOf(isSetType()).compareTo(typedOther.isSetType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.type, typedOther.type);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetActorId()).compareTo(typedOther.isSetActorId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetActorId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.actorId, typedOther.actorId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEntryId()).compareTo(typedOther.isSetEntryId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEntryId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.entryId, typedOther.entryId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEntryOwnerId()).compareTo(typedOther.isSetEntryOwnerId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEntryOwnerId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.entryOwnerId, typedOther.entryOwnerId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCommentId()).compareTo(typedOther.isSetCommentId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCommentId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.commentId, typedOther.commentId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetParams()).compareTo(typedOther.isSetParams());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParams()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.params, typedOther.params);
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
    StringBuilder sb = new StringBuilder("RecoverCommentRequest(");
    boolean first = true;

    sb.append("type:");
    if (this.type == null) {
      sb.append("null");
    } else {
      sb.append(this.type);
    }
    first = false;
    if (isSetActorId()) {
      if (!first) sb.append(", ");
      sb.append("actorId:");
      sb.append(this.actorId);
      first = false;
    }
    if (!first) sb.append(", ");
    sb.append("entryId:");
    sb.append(this.entryId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("entryOwnerId:");
    sb.append(this.entryOwnerId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("commentId:");
    sb.append(this.commentId);
    first = false;
    if (isSetParams()) {
      if (!first) sb.append(", ");
      sb.append("params:");
      if (this.params == null) {
        sb.append("null");
      } else {
        sb.append(this.params);
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

  private static class RecoverCommentRequestStandardSchemeFactory implements SchemeFactory {
    public RecoverCommentRequestStandardScheme getScheme() {
      return new RecoverCommentRequestStandardScheme();
    }
  }

  private static class RecoverCommentRequestStandardScheme extends StandardScheme<RecoverCommentRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RecoverCommentRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.type = com.renren.ugc.comment.xoa2.CommentType.findByValue(iprot.readI32());
              struct.setTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ACTOR_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.actorId = iprot.readI32();
              struct.setActorIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ENTRY_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.entryId = iprot.readI64();
              struct.setEntryIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // ENTRY_OWNER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.entryOwnerId = iprot.readI32();
              struct.setEntryOwnerIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // COMMENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.commentId = iprot.readI64();
              struct.setCommentIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // PARAMS
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map132 = iprot.readMapBegin();
                struct.params = new HashMap<String,String>(2*_map132.size);
                for (int _i133 = 0; _i133 < _map132.size; ++_i133)
                {
                  String _key134; // required
                  String _val135; // required
                  _key134 = iprot.readString();
                  _val135 = iprot.readString();
                  struct.params.put(_key134, _val135);
                }
                iprot.readMapEnd();
              }
              struct.setParamsIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, RecoverCommentRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.type != null) {
        oprot.writeFieldBegin(TYPE_FIELD_DESC);
        oprot.writeI32(struct.type.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.isSetActorId()) {
        oprot.writeFieldBegin(ACTOR_ID_FIELD_DESC);
        oprot.writeI32(struct.actorId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(ENTRY_ID_FIELD_DESC);
      oprot.writeI64(struct.entryId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(ENTRY_OWNER_ID_FIELD_DESC);
      oprot.writeI32(struct.entryOwnerId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(COMMENT_ID_FIELD_DESC);
      oprot.writeI64(struct.commentId);
      oprot.writeFieldEnd();
      if (struct.params != null) {
        if (struct.isSetParams()) {
          oprot.writeFieldBegin(PARAMS_FIELD_DESC);
          {
            oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, struct.params.size()));
            for (Map.Entry<String, String> _iter136 : struct.params.entrySet())
            {
              oprot.writeString(_iter136.getKey());
              oprot.writeString(_iter136.getValue());
            }
            oprot.writeMapEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RecoverCommentRequestTupleSchemeFactory implements SchemeFactory {
    public RecoverCommentRequestTupleScheme getScheme() {
      return new RecoverCommentRequestTupleScheme();
    }
  }

  private static class RecoverCommentRequestTupleScheme extends TupleScheme<RecoverCommentRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RecoverCommentRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetType()) {
        optionals.set(0);
      }
      if (struct.isSetActorId()) {
        optionals.set(1);
      }
      if (struct.isSetEntryId()) {
        optionals.set(2);
      }
      if (struct.isSetEntryOwnerId()) {
        optionals.set(3);
      }
      if (struct.isSetCommentId()) {
        optionals.set(4);
      }
      if (struct.isSetParams()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetType()) {
        oprot.writeI32(struct.type.getValue());
      }
      if (struct.isSetActorId()) {
        oprot.writeI32(struct.actorId);
      }
      if (struct.isSetEntryId()) {
        oprot.writeI64(struct.entryId);
      }
      if (struct.isSetEntryOwnerId()) {
        oprot.writeI32(struct.entryOwnerId);
      }
      if (struct.isSetCommentId()) {
        oprot.writeI64(struct.commentId);
      }
      if (struct.isSetParams()) {
        {
          oprot.writeI32(struct.params.size());
          for (Map.Entry<String, String> _iter137 : struct.params.entrySet())
          {
            oprot.writeString(_iter137.getKey());
            oprot.writeString(_iter137.getValue());
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RecoverCommentRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.type = com.renren.ugc.comment.xoa2.CommentType.findByValue(iprot.readI32());
        struct.setTypeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.actorId = iprot.readI32();
        struct.setActorIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.entryId = iprot.readI64();
        struct.setEntryIdIsSet(true);
      }
      if (incoming.get(3)) {
        struct.entryOwnerId = iprot.readI32();
        struct.setEntryOwnerIdIsSet(true);
      }
      if (incoming.get(4)) {
        struct.commentId = iprot.readI64();
        struct.setCommentIdIsSet(true);
      }
      if (incoming.get(5)) {
        {
          org.apache.thrift.protocol.TMap _map138 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.params = new HashMap<String,String>(2*_map138.size);
          for (int _i139 = 0; _i139 < _map138.size; ++_i139)
          {
            String _key140; // required
            String _val141; // required
            _key140 = iprot.readString();
            _val141 = iprot.readString();
            struct.params.put(_key140, _val141);
          }
        }
        struct.setParamsIsSet(true);
      }
    }
  }

}

