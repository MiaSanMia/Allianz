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

public class GetCommentResponse implements org.apache.thrift.TBase<GetCommentResponse, GetCommentResponse._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("GetCommentResponse");

  private static final org.apache.thrift.protocol.TField COMMENT_FIELD_DESC = new org.apache.thrift.protocol.TField("comment", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField BASE_REP_FIELD_DESC = new org.apache.thrift.protocol.TField("baseRep", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField ENTRY_FIELD_DESC = new org.apache.thrift.protocol.TField("entry", org.apache.thrift.protocol.TType.STRUCT, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new GetCommentResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new GetCommentResponseTupleSchemeFactory());
  }

  /**
   * 获取的评论
   */
  public com.renren.ugc.comment.xoa2.Comment comment; // optional
  /**
   * 响应信息
   */
  public com.renren.xoa2.BaseResponse baseRep; // optional
  /**
   * 被评论的“实体”
   */
  public com.renren.ugc.comment.xoa2.Entry entry; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 获取的评论
     */
    COMMENT((short)1, "comment"),
    /**
     * 响应信息
     */
    BASE_REP((short)2, "baseRep"),
    /**
     * 被评论的“实体”
     */
    ENTRY((short)3, "entry");

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
        case 1: // COMMENT
          return COMMENT;
        case 2: // BASE_REP
          return BASE_REP;
        case 3: // ENTRY
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
  private _Fields optionals[] = {_Fields.COMMENT,_Fields.BASE_REP,_Fields.ENTRY};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.COMMENT, new org.apache.thrift.meta_data.FieldMetaData("comment", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.ugc.comment.xoa2.Comment.class)));
    tmpMap.put(_Fields.BASE_REP, new org.apache.thrift.meta_data.FieldMetaData("baseRep", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.xoa2.BaseResponse.class)));
    tmpMap.put(_Fields.ENTRY, new org.apache.thrift.meta_data.FieldMetaData("entry", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.ugc.comment.xoa2.Entry.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(GetCommentResponse.class, metaDataMap);
  }

  public GetCommentResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public GetCommentResponse(GetCommentResponse other) {
    if (other.isSetComment()) {
      this.comment = new com.renren.ugc.comment.xoa2.Comment(other.comment);
    }
    if (other.isSetBaseRep()) {
      this.baseRep = new com.renren.xoa2.BaseResponse(other.baseRep);
    }
    if (other.isSetEntry()) {
      this.entry = new com.renren.ugc.comment.xoa2.Entry(other.entry);
    }
  }

  public GetCommentResponse deepCopy() {
    return new GetCommentResponse(this);
  }

  @Override
  public void clear() {
    this.comment = null;
    this.baseRep = null;
    this.entry = null;
  }

  /**
   * 获取的评论
   */
  public com.renren.ugc.comment.xoa2.Comment getComment() {
    return this.comment;
  }

  /**
   * 获取的评论
   */
  public GetCommentResponse setComment(com.renren.ugc.comment.xoa2.Comment comment) {
    this.comment = comment;
    return this;
  }

  public void unsetComment() {
    this.comment = null;
  }

  /** Returns true if field comment is set (has been assigned a value) and false otherwise */
  public boolean isSetComment() {
    return this.comment != null;
  }

  public void setCommentIsSet(boolean value) {
    if (!value) {
      this.comment = null;
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
  public GetCommentResponse setBaseRep(com.renren.xoa2.BaseResponse baseRep) {
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
   * 被评论的“实体”
   */
  public com.renren.ugc.comment.xoa2.Entry getEntry() {
    return this.entry;
  }

  /**
   * 被评论的“实体”
   */
  public GetCommentResponse setEntry(com.renren.ugc.comment.xoa2.Entry entry) {
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
    case COMMENT:
      if (value == null) {
        unsetComment();
      } else {
        setComment((com.renren.ugc.comment.xoa2.Comment)value);
      }
      break;

    case BASE_REP:
      if (value == null) {
        unsetBaseRep();
      } else {
        setBaseRep((com.renren.xoa2.BaseResponse)value);
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
    case COMMENT:
      return getComment();

    case BASE_REP:
      return getBaseRep();

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
    case COMMENT:
      return isSetComment();
    case BASE_REP:
      return isSetBaseRep();
    case ENTRY:
      return isSetEntry();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof GetCommentResponse)
      return this.equals((GetCommentResponse)that);
    return false;
  }

  public boolean equals(GetCommentResponse that) {
    if (that == null)
      return false;

    boolean this_present_comment = true && this.isSetComment();
    boolean that_present_comment = true && that.isSetComment();
    if (this_present_comment || that_present_comment) {
      if (!(this_present_comment && that_present_comment))
        return false;
      if (!this.comment.equals(that.comment))
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

    boolean present_comment = true && (isSetComment());
    builder.append(present_comment);
    if (present_comment)
      builder.append(comment);

    boolean present_baseRep = true && (isSetBaseRep());
    builder.append(present_baseRep);
    if (present_baseRep)
      builder.append(baseRep);

    boolean present_entry = true && (isSetEntry());
    builder.append(present_entry);
    if (present_entry)
      builder.append(entry);

    return builder.toHashCode();
  }

  public int compareTo(GetCommentResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    GetCommentResponse typedOther = (GetCommentResponse)other;

    lastComparison = Boolean.valueOf(isSetComment()).compareTo(typedOther.isSetComment());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetComment()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.comment, typedOther.comment);
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
    StringBuilder sb = new StringBuilder("GetCommentResponse(");
    boolean first = true;

    if (isSetComment()) {
      sb.append("comment:");
      if (this.comment == null) {
        sb.append("null");
      } else {
        sb.append(this.comment);
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class GetCommentResponseStandardSchemeFactory implements SchemeFactory {
    public GetCommentResponseStandardScheme getScheme() {
      return new GetCommentResponseStandardScheme();
    }
  }

  private static class GetCommentResponseStandardScheme extends StandardScheme<GetCommentResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, GetCommentResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // COMMENT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.comment = new com.renren.ugc.comment.xoa2.Comment();
              struct.comment.read(iprot);
              struct.setCommentIsSet(true);
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
          case 3: // ENTRY
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, GetCommentResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.comment != null) {
        if (struct.isSetComment()) {
          oprot.writeFieldBegin(COMMENT_FIELD_DESC);
          struct.comment.write(oprot);
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

  private static class GetCommentResponseTupleSchemeFactory implements SchemeFactory {
    public GetCommentResponseTupleScheme getScheme() {
      return new GetCommentResponseTupleScheme();
    }
  }

  private static class GetCommentResponseTupleScheme extends TupleScheme<GetCommentResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, GetCommentResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetComment()) {
        optionals.set(0);
      }
      if (struct.isSetBaseRep()) {
        optionals.set(1);
      }
      if (struct.isSetEntry()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetComment()) {
        struct.comment.write(oprot);
      }
      if (struct.isSetBaseRep()) {
        struct.baseRep.write(oprot);
      }
      if (struct.isSetEntry()) {
        struct.entry.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, GetCommentResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.comment = new com.renren.ugc.comment.xoa2.Comment();
        struct.comment.read(iprot);
        struct.setCommentIsSet(true);
      }
      if (incoming.get(1)) {
        struct.baseRep = new com.renren.xoa2.BaseResponse();
        struct.baseRep.read(iprot);
        struct.setBaseRepIsSet(true);
      }
      if (incoming.get(2)) {
        struct.entry = new com.renren.ugc.comment.xoa2.Entry();
        struct.entry.read(iprot);
        struct.setEntryIsSet(true);
      }
    }
  }

}

