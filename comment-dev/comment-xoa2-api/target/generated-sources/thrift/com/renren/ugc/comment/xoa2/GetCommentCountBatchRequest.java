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

/**
 * 批量获取多个entry评论的总数。注意，由于Sharding的限制，
 * 只支持一个对属于一个entryOwnerId的评论数量进行批量获取。
 */
public class GetCommentCountBatchRequest implements org.apache.thrift.TBase<GetCommentCountBatchRequest, GetCommentCountBatchRequest._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("GetCommentCountBatchRequest");

  private static final org.apache.thrift.protocol.TField TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("type", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField ACTOR_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("actorId", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField ENTRY_IDS_FIELD_DESC = new org.apache.thrift.protocol.TField("entryIds", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField ENTRY_OWNER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("entryOwnerId", org.apache.thrift.protocol.TType.I32, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new GetCommentCountBatchRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new GetCommentCountBatchRequestTupleSchemeFactory());
  }

  /**
   * 评论业务类型
   * 
   * @see com.renren.ugc.comment.xoa2.CommentType
   */
  public com.renren.ugc.comment.xoa2.CommentType type; // required
  /**
   * 获取评论总数的用户Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  public int actorId; // optional
  /**
   * 被评论实体Id
   */
  public List<Long> entryIds; // required
  /**
   * 被评论实体所有者Id
   */
  public int entryOwnerId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 评论业务类型
     * 
     * @see com.renren.ugc.comment.xoa2.CommentType
     */
    TYPE((short)1, "type"),
    /**
     * 获取评论总数的用户Id - 对于某些业务是可选的，但是
     * 对于某些业务（比如UGC Blog）是必须的，用于
     * 来校验权限
     */
    ACTOR_ID((short)2, "actorId"),
    /**
     * 被评论实体Id
     */
    ENTRY_IDS((short)3, "entryIds"),
    /**
     * 被评论实体所有者Id
     */
    ENTRY_OWNER_ID((short)4, "entryOwnerId");

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
        case 3: // ENTRY_IDS
          return ENTRY_IDS;
        case 4: // ENTRY_OWNER_ID
          return ENTRY_OWNER_ID;
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
  private static final int __ENTRYOWNERID_ISSET_ID = 1;
  private BitSet __isset_bit_vector = new BitSet(2);
  private _Fields optionals[] = {_Fields.ACTOR_ID};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TYPE, new org.apache.thrift.meta_data.FieldMetaData("type", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, com.renren.ugc.comment.xoa2.CommentType.class)));
    tmpMap.put(_Fields.ACTOR_ID, new org.apache.thrift.meta_data.FieldMetaData("actorId", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.ENTRY_IDS, new org.apache.thrift.meta_data.FieldMetaData("entryIds", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64))));
    tmpMap.put(_Fields.ENTRY_OWNER_ID, new org.apache.thrift.meta_data.FieldMetaData("entryOwnerId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(GetCommentCountBatchRequest.class, metaDataMap);
  }

  public GetCommentCountBatchRequest() {
  }

  public GetCommentCountBatchRequest(
    com.renren.ugc.comment.xoa2.CommentType type,
    List<Long> entryIds,
    int entryOwnerId)
  {
    this();
    this.type = type;
    this.entryIds = entryIds;
    this.entryOwnerId = entryOwnerId;
    setEntryOwnerIdIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public GetCommentCountBatchRequest(GetCommentCountBatchRequest other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetType()) {
      this.type = other.type;
    }
    this.actorId = other.actorId;
    if (other.isSetEntryIds()) {
      List<Long> __this__entryIds = new ArrayList<Long>();
      for (Long other_element : other.entryIds) {
        __this__entryIds.add(other_element);
      }
      this.entryIds = __this__entryIds;
    }
    this.entryOwnerId = other.entryOwnerId;
  }

  public GetCommentCountBatchRequest deepCopy() {
    return new GetCommentCountBatchRequest(this);
  }

  @Override
  public void clear() {
    this.type = null;
    setActorIdIsSet(false);
    this.actorId = 0;
    this.entryIds = null;
    setEntryOwnerIdIsSet(false);
    this.entryOwnerId = 0;
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
  public GetCommentCountBatchRequest setType(com.renren.ugc.comment.xoa2.CommentType type) {
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
   * 获取评论总数的用户Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  public int getActorId() {
    return this.actorId;
  }

  /**
   * 获取评论总数的用户Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  public GetCommentCountBatchRequest setActorId(int actorId) {
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

  public int getEntryIdsSize() {
    return (this.entryIds == null) ? 0 : this.entryIds.size();
  }

  public java.util.Iterator<Long> getEntryIdsIterator() {
    return (this.entryIds == null) ? null : this.entryIds.iterator();
  }

  public void addToEntryIds(long elem) {
    if (this.entryIds == null) {
      this.entryIds = new ArrayList<Long>();
    }
    this.entryIds.add(elem);
  }

  /**
   * 被评论实体Id
   */
  public List<Long> getEntryIds() {
    return this.entryIds;
  }

  /**
   * 被评论实体Id
   */
  public GetCommentCountBatchRequest setEntryIds(List<Long> entryIds) {
    this.entryIds = entryIds;
    return this;
  }

  public void unsetEntryIds() {
    this.entryIds = null;
  }

  /** Returns true if field entryIds is set (has been assigned a value) and false otherwise */
  public boolean isSetEntryIds() {
    return this.entryIds != null;
  }

  public void setEntryIdsIsSet(boolean value) {
    if (!value) {
      this.entryIds = null;
    }
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
  public GetCommentCountBatchRequest setEntryOwnerId(int entryOwnerId) {
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

    case ENTRY_IDS:
      if (value == null) {
        unsetEntryIds();
      } else {
        setEntryIds((List<Long>)value);
      }
      break;

    case ENTRY_OWNER_ID:
      if (value == null) {
        unsetEntryOwnerId();
      } else {
        setEntryOwnerId((Integer)value);
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

    case ENTRY_IDS:
      return getEntryIds();

    case ENTRY_OWNER_ID:
      return Integer.valueOf(getEntryOwnerId());

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
    case ENTRY_IDS:
      return isSetEntryIds();
    case ENTRY_OWNER_ID:
      return isSetEntryOwnerId();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof GetCommentCountBatchRequest)
      return this.equals((GetCommentCountBatchRequest)that);
    return false;
  }

  public boolean equals(GetCommentCountBatchRequest that) {
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

    boolean this_present_entryIds = true && this.isSetEntryIds();
    boolean that_present_entryIds = true && that.isSetEntryIds();
    if (this_present_entryIds || that_present_entryIds) {
      if (!(this_present_entryIds && that_present_entryIds))
        return false;
      if (!this.entryIds.equals(that.entryIds))
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

    boolean present_entryIds = true && (isSetEntryIds());
    builder.append(present_entryIds);
    if (present_entryIds)
      builder.append(entryIds);

    boolean present_entryOwnerId = true;
    builder.append(present_entryOwnerId);
    if (present_entryOwnerId)
      builder.append(entryOwnerId);

    return builder.toHashCode();
  }

  public int compareTo(GetCommentCountBatchRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    GetCommentCountBatchRequest typedOther = (GetCommentCountBatchRequest)other;

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
    lastComparison = Boolean.valueOf(isSetEntryIds()).compareTo(typedOther.isSetEntryIds());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEntryIds()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.entryIds, typedOther.entryIds);
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
    StringBuilder sb = new StringBuilder("GetCommentCountBatchRequest(");
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
    sb.append("entryIds:");
    if (this.entryIds == null) {
      sb.append("null");
    } else {
      sb.append(this.entryIds);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("entryOwnerId:");
    sb.append(this.entryOwnerId);
    first = false;
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

  private static class GetCommentCountBatchRequestStandardSchemeFactory implements SchemeFactory {
    public GetCommentCountBatchRequestStandardScheme getScheme() {
      return new GetCommentCountBatchRequestStandardScheme();
    }
  }

  private static class GetCommentCountBatchRequestStandardScheme extends StandardScheme<GetCommentCountBatchRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, GetCommentCountBatchRequest struct) throws org.apache.thrift.TException {
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
          case 3: // ENTRY_IDS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list172 = iprot.readListBegin();
                struct.entryIds = new ArrayList<Long>(_list172.size);
                for (int _i173 = 0; _i173 < _list172.size; ++_i173)
                {
                  long _elem174; // required
                  _elem174 = iprot.readI64();
                  struct.entryIds.add(_elem174);
                }
                iprot.readListEnd();
              }
              struct.setEntryIdsIsSet(true);
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
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, GetCommentCountBatchRequest struct) throws org.apache.thrift.TException {
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
      if (struct.entryIds != null) {
        oprot.writeFieldBegin(ENTRY_IDS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I64, struct.entryIds.size()));
          for (long _iter175 : struct.entryIds)
          {
            oprot.writeI64(_iter175);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(ENTRY_OWNER_ID_FIELD_DESC);
      oprot.writeI32(struct.entryOwnerId);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class GetCommentCountBatchRequestTupleSchemeFactory implements SchemeFactory {
    public GetCommentCountBatchRequestTupleScheme getScheme() {
      return new GetCommentCountBatchRequestTupleScheme();
    }
  }

  private static class GetCommentCountBatchRequestTupleScheme extends TupleScheme<GetCommentCountBatchRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, GetCommentCountBatchRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetType()) {
        optionals.set(0);
      }
      if (struct.isSetActorId()) {
        optionals.set(1);
      }
      if (struct.isSetEntryIds()) {
        optionals.set(2);
      }
      if (struct.isSetEntryOwnerId()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetType()) {
        oprot.writeI32(struct.type.getValue());
      }
      if (struct.isSetActorId()) {
        oprot.writeI32(struct.actorId);
      }
      if (struct.isSetEntryIds()) {
        {
          oprot.writeI32(struct.entryIds.size());
          for (long _iter176 : struct.entryIds)
          {
            oprot.writeI64(_iter176);
          }
        }
      }
      if (struct.isSetEntryOwnerId()) {
        oprot.writeI32(struct.entryOwnerId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, GetCommentCountBatchRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.type = com.renren.ugc.comment.xoa2.CommentType.findByValue(iprot.readI32());
        struct.setTypeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.actorId = iprot.readI32();
        struct.setActorIdIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TList _list177 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I64, iprot.readI32());
          struct.entryIds = new ArrayList<Long>(_list177.size);
          for (int _i178 = 0; _i178 < _list177.size; ++_i178)
          {
            long _elem179; // required
            _elem179 = iprot.readI64();
            struct.entryIds.add(_elem179);
          }
        }
        struct.setEntryIdsIsSet(true);
      }
      if (incoming.get(3)) {
        struct.entryOwnerId = iprot.readI32();
        struct.setEntryOwnerIdIsSet(true);
      }
    }
  }

}

