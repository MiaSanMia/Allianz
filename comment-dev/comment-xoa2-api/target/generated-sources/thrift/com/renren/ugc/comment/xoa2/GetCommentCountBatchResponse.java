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

public class GetCommentCountBatchResponse implements org.apache.thrift.TBase<GetCommentCountBatchResponse, GetCommentCountBatchResponse._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("GetCommentCountBatchResponse");

  private static final org.apache.thrift.protocol.TField COUNT_MAP_FIELD_DESC = new org.apache.thrift.protocol.TField("countMap", org.apache.thrift.protocol.TType.MAP, (short)1);
  private static final org.apache.thrift.protocol.TField BASE_REP_FIELD_DESC = new org.apache.thrift.protocol.TField("baseRep", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new GetCommentCountBatchResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new GetCommentCountBatchResponseTupleSchemeFactory());
  }

  /**
   * 评论总数
   */
  public Map<Long,Integer> countMap; // optional
  /**
   * 响应信息
   */
  public com.renren.xoa2.BaseResponse baseRep; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 评论总数
     */
    COUNT_MAP((short)1, "countMap"),
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
        case 1: // COUNT_MAP
          return COUNT_MAP;
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
  private _Fields optionals[] = {_Fields.COUNT_MAP,_Fields.BASE_REP};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.COUNT_MAP, new org.apache.thrift.meta_data.FieldMetaData("countMap", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32))));
    tmpMap.put(_Fields.BASE_REP, new org.apache.thrift.meta_data.FieldMetaData("baseRep", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.xoa2.BaseResponse.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(GetCommentCountBatchResponse.class, metaDataMap);
  }

  public GetCommentCountBatchResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public GetCommentCountBatchResponse(GetCommentCountBatchResponse other) {
    if (other.isSetCountMap()) {
      Map<Long,Integer> __this__countMap = new HashMap<Long,Integer>();
      for (Map.Entry<Long, Integer> other_element : other.countMap.entrySet()) {

        Long other_element_key = other_element.getKey();
        Integer other_element_value = other_element.getValue();

        Long __this__countMap_copy_key = other_element_key;

        Integer __this__countMap_copy_value = other_element_value;

        __this__countMap.put(__this__countMap_copy_key, __this__countMap_copy_value);
      }
      this.countMap = __this__countMap;
    }
    if (other.isSetBaseRep()) {
      this.baseRep = new com.renren.xoa2.BaseResponse(other.baseRep);
    }
  }

  public GetCommentCountBatchResponse deepCopy() {
    return new GetCommentCountBatchResponse(this);
  }

  @Override
  public void clear() {
    this.countMap = null;
    this.baseRep = null;
  }

  public int getCountMapSize() {
    return (this.countMap == null) ? 0 : this.countMap.size();
  }

  public void putToCountMap(long key, int val) {
    if (this.countMap == null) {
      this.countMap = new HashMap<Long,Integer>();
    }
    this.countMap.put(key, val);
  }

  /**
   * 评论总数
   */
  public Map<Long,Integer> getCountMap() {
    return this.countMap;
  }

  /**
   * 评论总数
   */
  public GetCommentCountBatchResponse setCountMap(Map<Long,Integer> countMap) {
    this.countMap = countMap;
    return this;
  }

  public void unsetCountMap() {
    this.countMap = null;
  }

  /** Returns true if field countMap is set (has been assigned a value) and false otherwise */
  public boolean isSetCountMap() {
    return this.countMap != null;
  }

  public void setCountMapIsSet(boolean value) {
    if (!value) {
      this.countMap = null;
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
  public GetCommentCountBatchResponse setBaseRep(com.renren.xoa2.BaseResponse baseRep) {
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
    case COUNT_MAP:
      if (value == null) {
        unsetCountMap();
      } else {
        setCountMap((Map<Long,Integer>)value);
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
    case COUNT_MAP:
      return getCountMap();

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
    case COUNT_MAP:
      return isSetCountMap();
    case BASE_REP:
      return isSetBaseRep();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof GetCommentCountBatchResponse)
      return this.equals((GetCommentCountBatchResponse)that);
    return false;
  }

  public boolean equals(GetCommentCountBatchResponse that) {
    if (that == null)
      return false;

    boolean this_present_countMap = true && this.isSetCountMap();
    boolean that_present_countMap = true && that.isSetCountMap();
    if (this_present_countMap || that_present_countMap) {
      if (!(this_present_countMap && that_present_countMap))
        return false;
      if (!this.countMap.equals(that.countMap))
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

    boolean present_countMap = true && (isSetCountMap());
    builder.append(present_countMap);
    if (present_countMap)
      builder.append(countMap);

    boolean present_baseRep = true && (isSetBaseRep());
    builder.append(present_baseRep);
    if (present_baseRep)
      builder.append(baseRep);

    return builder.toHashCode();
  }

  public int compareTo(GetCommentCountBatchResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    GetCommentCountBatchResponse typedOther = (GetCommentCountBatchResponse)other;

    lastComparison = Boolean.valueOf(isSetCountMap()).compareTo(typedOther.isSetCountMap());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCountMap()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.countMap, typedOther.countMap);
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
    StringBuilder sb = new StringBuilder("GetCommentCountBatchResponse(");
    boolean first = true;

    if (isSetCountMap()) {
      sb.append("countMap:");
      if (this.countMap == null) {
        sb.append("null");
      } else {
        sb.append(this.countMap);
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

  private static class GetCommentCountBatchResponseStandardSchemeFactory implements SchemeFactory {
    public GetCommentCountBatchResponseStandardScheme getScheme() {
      return new GetCommentCountBatchResponseStandardScheme();
    }
  }

  private static class GetCommentCountBatchResponseStandardScheme extends StandardScheme<GetCommentCountBatchResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, GetCommentCountBatchResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // COUNT_MAP
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map180 = iprot.readMapBegin();
                struct.countMap = new HashMap<Long,Integer>(2*_map180.size);
                for (int _i181 = 0; _i181 < _map180.size; ++_i181)
                {
                  long _key182; // required
                  int _val183; // required
                  _key182 = iprot.readI64();
                  _val183 = iprot.readI32();
                  struct.countMap.put(_key182, _val183);
                }
                iprot.readMapEnd();
              }
              struct.setCountMapIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, GetCommentCountBatchResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.countMap != null) {
        if (struct.isSetCountMap()) {
          oprot.writeFieldBegin(COUNT_MAP_FIELD_DESC);
          {
            oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I64, org.apache.thrift.protocol.TType.I32, struct.countMap.size()));
            for (Map.Entry<Long, Integer> _iter184 : struct.countMap.entrySet())
            {
              oprot.writeI64(_iter184.getKey());
              oprot.writeI32(_iter184.getValue());
            }
            oprot.writeMapEnd();
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

  private static class GetCommentCountBatchResponseTupleSchemeFactory implements SchemeFactory {
    public GetCommentCountBatchResponseTupleScheme getScheme() {
      return new GetCommentCountBatchResponseTupleScheme();
    }
  }

  private static class GetCommentCountBatchResponseTupleScheme extends TupleScheme<GetCommentCountBatchResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, GetCommentCountBatchResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetCountMap()) {
        optionals.set(0);
      }
      if (struct.isSetBaseRep()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetCountMap()) {
        {
          oprot.writeI32(struct.countMap.size());
          for (Map.Entry<Long, Integer> _iter185 : struct.countMap.entrySet())
          {
            oprot.writeI64(_iter185.getKey());
            oprot.writeI32(_iter185.getValue());
          }
        }
      }
      if (struct.isSetBaseRep()) {
        struct.baseRep.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, GetCommentCountBatchResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TMap _map186 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I64, org.apache.thrift.protocol.TType.I32, iprot.readI32());
          struct.countMap = new HashMap<Long,Integer>(2*_map186.size);
          for (int _i187 = 0; _i187 < _map186.size; ++_i187)
          {
            long _key188; // required
            int _val189; // required
            _key188 = iprot.readI64();
            _val189 = iprot.readI32();
            struct.countMap.put(_key188, _val189);
          }
        }
        struct.setCountMapIsSet(true);
      }
      if (incoming.get(1)) {
        struct.baseRep = new com.renren.xoa2.BaseResponse();
        struct.baseRep.read(iprot);
        struct.setBaseRepIsSet(true);
      }
    }
  }

}

