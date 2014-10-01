// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: merge.proto

package org.rocksdbprotobuf.proto;

public final class Merge {
  private Merge() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registry.add(org.rocksdbprotobuf.proto.Merge.mergeType);
    registry.add(org.rocksdbprotobuf.proto.Merge.mergeCap);
  }
  public interface MergeMessageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
  }
  /**
   * Protobuf type {@code MergeMessage}
   */
  public static final class MergeMessage extends
      com.google.protobuf.GeneratedMessage
      implements MergeMessageOrBuilder {
    // Use MergeMessage.newBuilder() to construct.
    private MergeMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MergeMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MergeMessage defaultInstance;
    public static MergeMessage getDefaultInstance() {
      return defaultInstance;
    }

    public MergeMessage getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MergeMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.rocksdbprotobuf.proto.Merge.internal_static_MergeMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.rocksdbprotobuf.proto.Merge.internal_static_MergeMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.rocksdbprotobuf.proto.Merge.MergeMessage.class, org.rocksdbprotobuf.proto.Merge.MergeMessage.Builder.class);
    }

    public static com.google.protobuf.Parser<MergeMessage> PARSER =
        new com.google.protobuf.AbstractParser<MergeMessage>() {
      public MergeMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MergeMessage(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MergeMessage> getParserForType() {
      return PARSER;
    }

    /**
     * Protobuf enum {@code MergeMessage.MergeType}
     */
    public enum MergeType
        implements com.google.protobuf.ProtocolMessageEnum {
      /**
       * <code>MERGE_LIST = 0;</code>
       */
      MERGE_LIST(0, 0),
      /**
       * <code>MERGE_CAPPED_LIST = 1;</code>
       */
      MERGE_CAPPED_LIST(1, 1),
      ;

      /**
       * <code>MERGE_LIST = 0;</code>
       */
      public static final int MERGE_LIST_VALUE = 0;
      /**
       * <code>MERGE_CAPPED_LIST = 1;</code>
       */
      public static final int MERGE_CAPPED_LIST_VALUE = 1;


      public final int getNumber() { return value; }

      public static MergeType valueOf(int value) {
        switch (value) {
          case 0: return MERGE_LIST;
          case 1: return MERGE_CAPPED_LIST;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<MergeType>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<MergeType>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<MergeType>() {
              public MergeType findValueByNumber(int number) {
                return MergeType.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return org.rocksdbprotobuf.proto.Merge.MergeMessage.getDescriptor().getEnumTypes().get(0);
      }

      private static final MergeType[] VALUES = values();

      public static MergeType valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private MergeType(int index, int value) {
        this.index = index;
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:MergeMessage.MergeType)
    }

    private void initFields() {
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.rocksdbprotobuf.proto.Merge.MergeMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(org.rocksdbprotobuf.proto.Merge.MergeMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code MergeMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements org.rocksdbprotobuf.proto.Merge.MergeMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return org.rocksdbprotobuf.proto.Merge.internal_static_MergeMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.rocksdbprotobuf.proto.Merge.internal_static_MergeMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.rocksdbprotobuf.proto.Merge.MergeMessage.class, org.rocksdbprotobuf.proto.Merge.MergeMessage.Builder.class);
      }

      // Construct using org.rocksdbprotobuf.proto.Merge.MergeMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.rocksdbprotobuf.proto.Merge.internal_static_MergeMessage_descriptor;
      }

      public org.rocksdbprotobuf.proto.Merge.MergeMessage getDefaultInstanceForType() {
        return org.rocksdbprotobuf.proto.Merge.MergeMessage.getDefaultInstance();
      }

      public org.rocksdbprotobuf.proto.Merge.MergeMessage build() {
        org.rocksdbprotobuf.proto.Merge.MergeMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public org.rocksdbprotobuf.proto.Merge.MergeMessage buildPartial() {
        org.rocksdbprotobuf.proto.Merge.MergeMessage result = new org.rocksdbprotobuf.proto.Merge.MergeMessage(this);
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.rocksdbprotobuf.proto.Merge.MergeMessage) {
          return mergeFrom((org.rocksdbprotobuf.proto.Merge.MergeMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.rocksdbprotobuf.proto.Merge.MergeMessage other) {
        if (other == org.rocksdbprotobuf.proto.Merge.MergeMessage.getDefaultInstance()) return this;
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        org.rocksdbprotobuf.proto.Merge.MergeMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (org.rocksdbprotobuf.proto.Merge.MergeMessage) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      // @@protoc_insertion_point(builder_scope:MergeMessage)
    }

    static {
      defaultInstance = new MergeMessage(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:MergeMessage)
  }

  public static final int MERGE_TYPE_FIELD_NUMBER = 50001;
  /**
   * <code>extend .google.protobuf.FieldOptions { ... }</code>
   */
  public static final
    com.google.protobuf.GeneratedMessage.GeneratedExtension<
      com.google.protobuf.DescriptorProtos.FieldOptions,
      org.rocksdbprotobuf.proto.Merge.MergeMessage.MergeType> mergeType = com.google.protobuf.GeneratedMessage
          .newFileScopedGeneratedExtension(
        org.rocksdbprotobuf.proto.Merge.MergeMessage.MergeType.class,
        null);
  public static final int MERGE_CAP_FIELD_NUMBER = 50002;
  /**
   * <code>extend .google.protobuf.FieldOptions { ... }</code>
   */
  public static final
    com.google.protobuf.GeneratedMessage.GeneratedExtension<
      com.google.protobuf.DescriptorProtos.FieldOptions,
      java.lang.Integer> mergeCap = com.google.protobuf.GeneratedMessage
          .newFileScopedGeneratedExtension(
        java.lang.Integer.class,
        null);
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_MergeMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MergeMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\013merge.proto\032 google/protobuf/descripto" +
      "r.proto\"B\n\014MergeMessage\"2\n\tMergeType\022\016\n\n" +
      "MERGE_LIST\020\000\022\025\n\021MERGE_CAPPED_LIST\020\001:L\n\nm" +
      "erge_type\022\035.google.protobuf.FieldOptions" +
      "\030\321\206\003 \001(\0162\027.MergeMessage.MergeType:2\n\tmer" +
      "ge_cap\022\035.google.protobuf.FieldOptions\030\322\206" +
      "\003 \001(\rB\033\n\031org.rocksdbprotobuf.proto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_MergeMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_MergeMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_MergeMessage_descriptor,
              new java.lang.String[] { });
          mergeType.internalInit(descriptor.getExtensions().get(0));
          mergeCap.internalInit(descriptor.getExtensions().get(1));
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.DescriptorProtos.getDescriptor(),
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
