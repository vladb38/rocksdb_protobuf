// A merge operator capable of merging protobufs
// @author vbalan
package org.rocksdbprotobuf;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import org.rocksdb.MergeOperator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * MergeOperator holds an operator to be applied when compacting
 * two values held under the same key in order to obtain a single
 * value.
 */
public class ProtobufMergeOperator implements MergeOperator {

	byte[] fileDescriptorSetBytes;

	public ProtobufMergeOperator() {
		this(null);
	}

	public ProtobufMergeOperator(Class<? extends GeneratedMessage> clazz) {
		if (clazz != null) {
            Descriptors.Descriptor descriptor = null;
            try {
				Method method = clazz.getMethod("getDescriptor", null);
				Object o = method.invoke(null);
				descriptor = (Descriptors.Descriptor) o;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (descriptor != null) {
				Descriptors.FileDescriptor fileDescriptor = descriptor.getFile();
				List<Descriptors.FileDescriptor> dependenciesDescriptors = fileDescriptor.getDependencies();
				DescriptorProtos.FileDescriptorSet.Builder fileDescriptorSetBuilder;
				fileDescriptorSetBuilder = DescriptorProtos.FileDescriptorSet.newBuilder();
                for (int i = dependenciesDescriptors.size()-1; i >= 0; i--) {
					fileDescriptorSetBuilder.addFile(dependenciesDescriptors.get(i).toProto());
				}
				fileDescriptorSetBuilder.addFile(fileDescriptor.toProto());
				DescriptorProtos.FileDescriptorSet fileDescriptorSet;
				fileDescriptorSet = fileDescriptorSetBuilder.build();
				fileDescriptorSetBytes = fileDescriptorSet.toByteArray();
			}
		}
	}

    @Override public long newMergeOperatorHandle() {
		return newMergeOperatorHandleImpl(fileDescriptorSetBytes);
	}

    private native long newMergeOperatorHandleImpl(byte[] fileDescriptorSetBytes);

}
