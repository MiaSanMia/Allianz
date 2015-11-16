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

public class RemoveAllCommentResponse implements org.apache.thrift.TBase<RemoveAllCommentResponse, RemoveAllCommentResponse._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RemoveAllCommentResponse");

  private static final org.apache.thrift.protocol.TField ALL_REMOVED_FIELD_DESC = new org.apache.thrift.protocol.TField("allRemoved", org.apache.thrift.protocol.TType.BOOL, (short)1);
  private static final org.apache.thrift.protocol.TField BASE_REP_FIELD_DESC = new org.apache.thrift.protocol.TField("baseRep", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RemoveAllCommentResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RemoveAllCommentResponseTupleSchemeFactory());
  }

  /**
   * 是否已全部删除
   */
  public boolean allRemoved; // optional
  /**
   * 响应信息
   */
  public com.renren.xoa2.BaseResponse baseRep; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 是否已全部删除
     */
    ALL_REMOVED((short)1, "allRemoved"),
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
        case 1: // ALL_REMOVED
          return ALL_REMOVED;
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
  private static final int __ALLREMOVED_ISSET_ID = 0;
  private BitSet __isset_bit_vector = new BitSet(1);
  private _Fields optionals[] = {_Fields.ALL_REMOVED,_Fields.BASE_REP};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ALL_REMOVED, new org.apache.thrift.meta_data.FieldMetaData("allRemoved", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.BASE_REP, new org.apache.thrift.meta_data.FieldMetaData("baseRep", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.xoa2.BaseResponse.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RemoveAllCommentResponse.class, metaDataMap);
  }

  public RemoveAllCommentResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RemoveAllCommentResponse(RemoveAllCommentResponse other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.allRemoved = other.allRemoved;
    if (other.isSetBaseRep()) {
      this.baseRep = new com.renren.xoa2.BaseResponse(other.baseRep);
    }
  }

  public RemoveAllCommentResponse deepCopy() {
    return new RemoveAllCommentResponse(this);
  }

  @Override
  public void clear() {
    setAllRemovedIsSet(false);
    this.allRemoved = false;
    this.baseRep = null;
  }

  /**
   * 是否已全部删除
   */
  public boolean isAllRemoved() {
    return this.allRemoved;
  }

  /**
   * 是否已全部删除
   */
  public RemoveAllCommentResponse setAllRemoved(boolean allRemoved) {
    this.allRemoved = allRemoved;
    setAllRemovedIsSet(true);
    return this;
  }

  public void unsetAllRemoved() {
    __isset_bit_vector.clear(__ALLREMOVED_ISSET_ID);
  }

  /** Returns true if field allRemoved is set (has been assigned a value) and false otherwise */
  public boolean isSetAllRemoved() {
    return __isset_bit_vector.get(__ALLREMOVED_ISSET_ID);
  }

  public void setAllRemovedIsSet(boolean value) {
    __isset_bit_vector.set(__ALLREMOVED_ISSET_ID, value);
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
  public RemoveAllCommentResponse setBaseRep(com.renren.xoa2.BaseResponse baseRep) {
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
    case ALL_REMOVED:
      if (value == null) {
        unsetAllRemoved();
      } else {
        setAllRemoved((Boolean)value);
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
    case ALL_REMOVED:
      return Boolean.valueOf(isAllRemoved());

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
    case ALL_REMOVED:
      return isSetAllRemoved();
    case BASE_REP:
      return isSetBaseRep();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof RemoveAllCommentResponse)
      return this.equals((RemoveAllCommentResponse)that);
    return false;
  }

  public boolean equals(RemoveAllCommentResponse that) {
    if (that == null)
      return false;

    boolean this_present_allRemoved = true && this.isSetAllRemoved();
    boolean that_present_allRemoved = true && that.isSetAllRemoved();
    if (this_present_allRemoved || that_present_allRemoved) {
      if (!(this_present_allRemoved && that_present_allRemoved))
        return false;
      if (this.allRemoved != that.allRemoved)
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

    boolean present_allRemoved = true && (isSetAllRemoved());
    builder.append(present_allRemoved);
    if (present_allRemoved)
      builder.append(allRemoved);

    boolean present_baseRep = true && (isSetBaseRep());
    builder.append(present_baseRep);
    if (present_baseRep)
      builder.append(baseRep);

    return builder.toHashCode();
  }

  public int compareTo(RemoveAllCommentResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    RemoveAllCommentResponse typedOther = (RemoveAllCommentResponse)other;

    lastComparison = Boolean.valueOf(isSetAllRemoved()).compareTo(typedOther.isSetAllRemoved());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAllRemoved()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.allRemoved, typedOther.allRemoved);
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
    StringBuilder sb = new StringBuilder("RemoveAllCommentResponse(");
    boolean first = true;

    if (isSetAllRemoved()) {
      sb.append("allRemoved:");
      sb.append(this.allRemoved);
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RemoveAllCommentResponseStandardSchemeFactory implements SchemeFactory {
    public RemoveAllCommentResponseStandardScheme getScheme() {
      return new RemoveAllCommentResponseStandardScheme();
    }
  }

  private static class RemoveAllCommentResponseStandardScheme extends StandardScheme<RemoveAllCommentResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RemoveAllCommentResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ALL_REMOVED
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.allRemoved = iprot.readBool();
              struct.setAllRemovedIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, RemoveAllCommentResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.isSetAllRemoved()) {
        oprot.writeFieldBegin(ALL_REMOVED_FIELD_DESC);
        oprot.writeBool(struct.allRemoved);
        oprot.writeFieldEnd();
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

  private static class RemoveAllCommentResponseTupleSchemeFactory implements SchemeFactory {
    public RemoveAllCommentResponseTupleScheme getScheme() {
      return new RemoveAllCommentResponseTupleScheme();
    }
  }

  private static class RemoveAllCommentResponseTupleScheme extends TupleScheme<RemoveAllCommentResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RemoveAllCommentResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetAllRemoved()) {
        optionals.set(0);
      }
      if (struct.isSetBaseRep()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetAllRemoved()) {
        oprot.writeBool(struct.allRemoved);
      }
      if (struct.isSetBaseRep()) {
        struct.baseRep.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RemoveAllCommentResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.allRemoved = iprot.readBool();
        struct.setAllRemovedIsSet(true);
      }
      if (incoming.get(1)) {
        struct.baseRep = new com.renren.xoa2.BaseResponse();
        struct.baseRep.read(iprot);
        struct.setBaseRepIsSet(true);
      }
    }
  }

}

