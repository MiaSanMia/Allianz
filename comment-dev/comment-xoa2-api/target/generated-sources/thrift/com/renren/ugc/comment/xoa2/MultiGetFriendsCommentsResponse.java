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

public class MultiGetFriendsCommentsResponse implements org.apache.thrift.TBase<MultiGetFriendsCommentsResponse, MultiGetFriendsCommentsResponse._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MultiGetFriendsCommentsResponse");

  private static final org.apache.thrift.protocol.TField FRIENDS_COMMENTS_FIELD_DESC = new org.apache.thrift.protocol.TField("friendsComments", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField BASE_REP_FIELD_DESC = new org.apache.thrift.protocol.TField("baseRep", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new MultiGetFriendsCommentsResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new MultiGetFriendsCommentsResponseTupleSchemeFactory());
  }

  /**
   * 新创建的评论列表  FIXME : key 去掉feed信息
   */
  public List<com.renren.ugc.comment.xoa2.FriendsCommentsResult> friendsComments; // optional
  /**
   * 响应信息
   */
  public com.renren.xoa2.BaseResponse baseRep; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 新创建的评论列表  FIXME : key 去掉feed信息
     */
    FRIENDS_COMMENTS((short)1, "friendsComments"),
    /**
     * 响应信息
     */
    BASE_REP((short)2, "baseRep");

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
        case 1: // FRIENDS_COMMENTS
          return FRIENDS_COMMENTS;
        case 2: // BASE_REP
          return BASE_REP;
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
  private _Fields optionals[] = {_Fields.FRIENDS_COMMENTS,_Fields.BASE_REP};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.FRIENDS_COMMENTS, new org.apache.thrift.meta_data.FieldMetaData("friendsComments", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.ugc.comment.xoa2.FriendsCommentsResult.class))));
    tmpMap.put(_Fields.BASE_REP, new org.apache.thrift.meta_data.FieldMetaData("baseRep", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.xoa2.BaseResponse.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MultiGetFriendsCommentsResponse.class, metaDataMap);
  }

  public MultiGetFriendsCommentsResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MultiGetFriendsCommentsResponse(MultiGetFriendsCommentsResponse other) {
    if (other.isSetFriendsComments()) {
      List<com.renren.ugc.comment.xoa2.FriendsCommentsResult> __this__friendsComments = new ArrayList<com.renren.ugc.comment.xoa2.FriendsCommentsResult>();
      for (com.renren.ugc.comment.xoa2.FriendsCommentsResult other_element : other.friendsComments) {
        __this__friendsComments.add(new com.renren.ugc.comment.xoa2.FriendsCommentsResult(other_element));
      }
      this.friendsComments = __this__friendsComments;
    }
    if (other.isSetBaseRep()) {
      this.baseRep = new com.renren.xoa2.BaseResponse(other.baseRep);
    }
  }

  public MultiGetFriendsCommentsResponse deepCopy() {
    return new MultiGetFriendsCommentsResponse(this);
  }

  @Override
  public void clear() {
    this.friendsComments = null;
    this.baseRep = null;
  }

  public int getFriendsCommentsSize() {
    return (this.friendsComments == null) ? 0 : this.friendsComments.size();
  }

  public java.util.Iterator<com.renren.ugc.comment.xoa2.FriendsCommentsResult> getFriendsCommentsIterator() {
    return (this.friendsComments == null) ? null : this.friendsComments.iterator();
  }

  public void addToFriendsComments(com.renren.ugc.comment.xoa2.FriendsCommentsResult elem) {
    if (this.friendsComments == null) {
      this.friendsComments = new ArrayList<com.renren.ugc.comment.xoa2.FriendsCommentsResult>();
    }
    this.friendsComments.add(elem);
  }

  /**
   * 新创建的评论列表  FIXME : key 去掉feed信息
   */
  public List<com.renren.ugc.comment.xoa2.FriendsCommentsResult> getFriendsComments() {
    return this.friendsComments;
  }

  /**
   * 新创建的评论列表  FIXME : key 去掉feed信息
   */
  public MultiGetFriendsCommentsResponse setFriendsComments(List<com.renren.ugc.comment.xoa2.FriendsCommentsResult> friendsComments) {
    this.friendsComments = friendsComments;
    return this;
  }

  public void unsetFriendsComments() {
    this.friendsComments = null;
  }

  /** Returns true if field friendsComments is set (has been assigned a value) and false otherwise */
  public boolean isSetFriendsComments() {
    return this.friendsComments != null;
  }

  public void setFriendsCommentsIsSet(boolean value) {
    if (!value) {
      this.friendsComments = null;
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
  public MultiGetFriendsCommentsResponse setBaseRep(com.renren.xoa2.BaseResponse baseRep) {
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

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case FRIENDS_COMMENTS:
      if (value == null) {
        unsetFriendsComments();
      } else {
        setFriendsComments((List<com.renren.ugc.comment.xoa2.FriendsCommentsResult>)value);
      }
      break;

    case BASE_REP:
      if (value == null) {
        unsetBaseRep();
      } else {
        setBaseRep((com.renren.xoa2.BaseResponse)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case FRIENDS_COMMENTS:
      return getFriendsComments();

    case BASE_REP:
      return getBaseRep();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case FRIENDS_COMMENTS:
      return isSetFriendsComments();
    case BASE_REP:
      return isSetBaseRep();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MultiGetFriendsCommentsResponse)
      return this.equals((MultiGetFriendsCommentsResponse)that);
    return false;
  }

  public boolean equals(MultiGetFriendsCommentsResponse that) {
    if (that == null)
      return false;

    boolean this_present_friendsComments = true && this.isSetFriendsComments();
    boolean that_present_friendsComments = true && that.isSetFriendsComments();
    if (this_present_friendsComments || that_present_friendsComments) {
      if (!(this_present_friendsComments && that_present_friendsComments))
        return false;
      if (!this.friendsComments.equals(that.friendsComments))
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

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_friendsComments = true && (isSetFriendsComments());
    builder.append(present_friendsComments);
    if (present_friendsComments)
      builder.append(friendsComments);

    boolean present_baseRep = true && (isSetBaseRep());
    builder.append(present_baseRep);
    if (present_baseRep)
      builder.append(baseRep);

    return builder.toHashCode();
  }

  public int compareTo(MultiGetFriendsCommentsResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    MultiGetFriendsCommentsResponse typedOther = (MultiGetFriendsCommentsResponse)other;

    lastComparison = Boolean.valueOf(isSetFriendsComments()).compareTo(typedOther.isSetFriendsComments());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFriendsComments()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.friendsComments, typedOther.friendsComments);
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
    StringBuilder sb = new StringBuilder("MultiGetFriendsCommentsResponse(");
    boolean first = true;

    if (isSetFriendsComments()) {
      sb.append("friendsComments:");
      if (this.friendsComments == null) {
        sb.append("null");
      } else {
        sb.append(this.friendsComments);
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class MultiGetFriendsCommentsResponseStandardSchemeFactory implements SchemeFactory {
    public MultiGetFriendsCommentsResponseStandardScheme getScheme() {
      return new MultiGetFriendsCommentsResponseStandardScheme();
    }
  }

  private static class MultiGetFriendsCommentsResponseStandardScheme extends StandardScheme<MultiGetFriendsCommentsResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, MultiGetFriendsCommentsResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // FRIENDS_COMMENTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list432 = iprot.readListBegin();
                struct.friendsComments = new ArrayList<com.renren.ugc.comment.xoa2.FriendsCommentsResult>(_list432.size);
                for (int _i433 = 0; _i433 < _list432.size; ++_i433)
                {
                  com.renren.ugc.comment.xoa2.FriendsCommentsResult _elem434; // required
                  _elem434 = new com.renren.ugc.comment.xoa2.FriendsCommentsResult();
                  _elem434.read(iprot);
                  struct.friendsComments.add(_elem434);
                }
                iprot.readListEnd();
              }
              struct.setFriendsCommentsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // BASE_REP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.baseRep = new com.renren.xoa2.BaseResponse();
              struct.baseRep.read(iprot);
              struct.setBaseRepIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, MultiGetFriendsCommentsResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.friendsComments != null) {
        if (struct.isSetFriendsComments()) {
          oprot.writeFieldBegin(FRIENDS_COMMENTS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.friendsComments.size()));
            for (com.renren.ugc.comment.xoa2.FriendsCommentsResult _iter435 : struct.friendsComments)
            {
              _iter435.write(oprot);
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
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MultiGetFriendsCommentsResponseTupleSchemeFactory implements SchemeFactory {
    public MultiGetFriendsCommentsResponseTupleScheme getScheme() {
      return new MultiGetFriendsCommentsResponseTupleScheme();
    }
  }

  private static class MultiGetFriendsCommentsResponseTupleScheme extends TupleScheme<MultiGetFriendsCommentsResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, MultiGetFriendsCommentsResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetFriendsComments()) {
        optionals.set(0);
      }
      if (struct.isSetBaseRep()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetFriendsComments()) {
        {
          oprot.writeI32(struct.friendsComments.size());
          for (com.renren.ugc.comment.xoa2.FriendsCommentsResult _iter436 : struct.friendsComments)
          {
            _iter436.write(oprot);
          }
        }
      }
      if (struct.isSetBaseRep()) {
        struct.baseRep.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, MultiGetFriendsCommentsResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list437 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.friendsComments = new ArrayList<com.renren.ugc.comment.xoa2.FriendsCommentsResult>(_list437.size);
          for (int _i438 = 0; _i438 < _list437.size; ++_i438)
          {
            com.renren.ugc.comment.xoa2.FriendsCommentsResult _elem439; // required
            _elem439 = new com.renren.ugc.comment.xoa2.FriendsCommentsResult();
            _elem439.read(iprot);
            struct.friendsComments.add(_elem439);
          }
        }
        struct.setFriendsCommentsIsSet(true);
      }
      if (incoming.get(1)) {
        struct.baseRep = new com.renren.xoa2.BaseResponse();
        struct.baseRep.read(iprot);
        struct.setBaseRepIsSet(true);
      }
    }
  }

}

