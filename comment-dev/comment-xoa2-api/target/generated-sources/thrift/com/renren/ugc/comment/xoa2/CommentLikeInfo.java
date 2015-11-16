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
 * 喜欢的数据
 */
public class CommentLikeInfo implements org.apache.thrift.TBase<CommentLikeInfo, CommentLikeInfo._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("CommentLikeInfo");

  private static final org.apache.thrift.protocol.TField LIKED_FIELD_DESC = new org.apache.thrift.protocol.TField("liked", org.apache.thrift.protocol.TType.BOOL, (short)1);
  private static final org.apache.thrift.protocol.TField LIKE_COUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("likeCount", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new CommentLikeInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new CommentLikeInfoTupleSchemeFactory());
  }

  /**
   * 是否被喜欢
   */
  public boolean liked; // required
  /**
   * 被喜欢的数量
   */
  public int likeCount; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 是否被喜欢
     */
    LIKED((short)1, "liked"),
    /**
     * 被喜欢的数量
     */
    LIKE_COUNT((short)2, "likeCount");

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
        case 1: // LIKED
          return LIKED;
        case 2: // LIKE_COUNT
          return LIKE_COUNT;
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
  private static final int __LIKED_ISSET_ID = 0;
  private static final int __LIKECOUNT_ISSET_ID = 1;
  private BitSet __isset_bit_vector = new BitSet(2);
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LIKED, new org.apache.thrift.meta_data.FieldMetaData("liked", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.LIKE_COUNT, new org.apache.thrift.meta_data.FieldMetaData("likeCount", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(CommentLikeInfo.class, metaDataMap);
  }

  public CommentLikeInfo() {
  }

  public CommentLikeInfo(
    boolean liked,
    int likeCount)
  {
    this();
    this.liked = liked;
    setLikedIsSet(true);
    this.likeCount = likeCount;
    setLikeCountIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public CommentLikeInfo(CommentLikeInfo other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.liked = other.liked;
    this.likeCount = other.likeCount;
  }

  public CommentLikeInfo deepCopy() {
    return new CommentLikeInfo(this);
  }

  @Override
  public void clear() {
    setLikedIsSet(false);
    this.liked = false;
    setLikeCountIsSet(false);
    this.likeCount = 0;
  }

  /**
   * 是否被喜欢
   */
  public boolean isLiked() {
    return this.liked;
  }

  /**
   * 是否被喜欢
   */
  public CommentLikeInfo setLiked(boolean liked) {
    this.liked = liked;
    setLikedIsSet(true);
    return this;
  }

  public void unsetLiked() {
    __isset_bit_vector.clear(__LIKED_ISSET_ID);
  }

  /** Returns true if field liked is set (has been assigned a value) and false otherwise */
  public boolean isSetLiked() {
    return __isset_bit_vector.get(__LIKED_ISSET_ID);
  }

  public void setLikedIsSet(boolean value) {
    __isset_bit_vector.set(__LIKED_ISSET_ID, value);
  }

  /**
   * 被喜欢的数量
   */
  public int getLikeCount() {
    return this.likeCount;
  }

  /**
   * 被喜欢的数量
   */
  public CommentLikeInfo setLikeCount(int likeCount) {
    this.likeCount = likeCount;
    setLikeCountIsSet(true);
    return this;
  }

  public void unsetLikeCount() {
    __isset_bit_vector.clear(__LIKECOUNT_ISSET_ID);
  }

  /** Returns true if field likeCount is set (has been assigned a value) and false otherwise */
  public boolean isSetLikeCount() {
    return __isset_bit_vector.get(__LIKECOUNT_ISSET_ID);
  }

  public void setLikeCountIsSet(boolean value) {
    __isset_bit_vector.set(__LIKECOUNT_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case LIKED:
      if (value == null) {
        unsetLiked();
      } else {
        setLiked((Boolean)value);
      }
      break;

    case LIKE_COUNT:
      if (value == null) {
        unsetLikeCount();
      } else {
        setLikeCount((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case LIKED:
      return Boolean.valueOf(isLiked());

    case LIKE_COUNT:
      return Integer.valueOf(getLikeCount());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case LIKED:
      return isSetLiked();
    case LIKE_COUNT:
      return isSetLikeCount();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof CommentLikeInfo)
      return this.equals((CommentLikeInfo)that);
    return false;
  }

  public boolean equals(CommentLikeInfo that) {
    if (that == null)
      return false;

    boolean this_present_liked = true;
    boolean that_present_liked = true;
    if (this_present_liked || that_present_liked) {
      if (!(this_present_liked && that_present_liked))
        return false;
      if (this.liked != that.liked)
        return false;
    }

    boolean this_present_likeCount = true;
    boolean that_present_likeCount = true;
    if (this_present_likeCount || that_present_likeCount) {
      if (!(this_present_likeCount && that_present_likeCount))
        return false;
      if (this.likeCount != that.likeCount)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_liked = true;
    builder.append(present_liked);
    if (present_liked)
      builder.append(liked);

    boolean present_likeCount = true;
    builder.append(present_likeCount);
    if (present_likeCount)
      builder.append(likeCount);

    return builder.toHashCode();
  }

  public int compareTo(CommentLikeInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    CommentLikeInfo typedOther = (CommentLikeInfo)other;

    lastComparison = Boolean.valueOf(isSetLiked()).compareTo(typedOther.isSetLiked());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLiked()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.liked, typedOther.liked);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLikeCount()).compareTo(typedOther.isSetLikeCount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLikeCount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.likeCount, typedOther.likeCount);
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
    StringBuilder sb = new StringBuilder("CommentLikeInfo(");
    boolean first = true;

    sb.append("liked:");
    sb.append(this.liked);
    first = false;
    if (!first) sb.append(", ");
    sb.append("likeCount:");
    sb.append(this.likeCount);
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

  private static class CommentLikeInfoStandardSchemeFactory implements SchemeFactory {
    public CommentLikeInfoStandardScheme getScheme() {
      return new CommentLikeInfoStandardScheme();
    }
  }

  private static class CommentLikeInfoStandardScheme extends StandardScheme<CommentLikeInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, CommentLikeInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LIKED
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.liked = iprot.readBool();
              struct.setLikedIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // LIKE_COUNT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.likeCount = iprot.readI32();
              struct.setLikeCountIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, CommentLikeInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(LIKED_FIELD_DESC);
      oprot.writeBool(struct.liked);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(LIKE_COUNT_FIELD_DESC);
      oprot.writeI32(struct.likeCount);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class CommentLikeInfoTupleSchemeFactory implements SchemeFactory {
    public CommentLikeInfoTupleScheme getScheme() {
      return new CommentLikeInfoTupleScheme();
    }
  }

  private static class CommentLikeInfoTupleScheme extends TupleScheme<CommentLikeInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, CommentLikeInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetLiked()) {
        optionals.set(0);
      }
      if (struct.isSetLikeCount()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetLiked()) {
        oprot.writeBool(struct.liked);
      }
      if (struct.isSetLikeCount()) {
        oprot.writeI32(struct.likeCount);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, CommentLikeInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.liked = iprot.readBool();
        struct.setLikedIsSet(true);
      }
      if (incoming.get(1)) {
        struct.likeCount = iprot.readI32();
        struct.setLikeCountIsSet(true);
      }
    }
  }

}

