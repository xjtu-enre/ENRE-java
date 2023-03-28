package entity.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import entity.dto.*;

public class EntityDTOAdapterFactory implements TypeAdapterFactory {

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (type.getRawType().equals(ExternalEntityDTO.class)) {
      return (TypeAdapter<T>) new ExternalEntityDTOAdapter();
    } else if (type.getRawType().equals(PackageEntityDTO.class)) {
      return (TypeAdapter<T>) new PackageEntityDTOAdapter();
    } else if (type.getRawType().equals(FileEntityDTO.class)) {
      return (TypeAdapter<T>) new FileEntityDTOAdapter();
    } else if (type.getRawType().equals(ClassEntityDTO.class)) {
      return (TypeAdapter<T>) new ClassEntityDTOAdapter();
    } else if (type.getRawType().equals(AnonymousClassEntityDTO.class)) {
      return (TypeAdapter<T>) new AnonymousClassEntityDTOAdapter();
    } else if (type.getRawType().equals(EnumEntityDTO.class)) {
      return (TypeAdapter<T>) new EnumEntityDTOAdapter();
    } else if (type.getRawType().equals(EnumConstantEntityDTO.class)) {
      return (TypeAdapter<T>) new EnumConstantEntityDTOAdapter();
    } else if (type.getRawType().equals(AnnotationEntityDTO.class)) {
      return (TypeAdapter<T>) new AnnotationEntityDTOAdapter();
    } else if (type.getRawType().equals(AnnotationMemberEntityDTO.class)) {
      return (TypeAdapter<T>) new AnnotationMemberEntityDTOAdapter();
    } else if (type.getRawType().equals(InterfaceEntityDTO.class)) {
      return (TypeAdapter<T>) new InterfaceEntityDTOAdapter();
    } else if (type.getRawType().equals(MethodEntityDTO.class)) {
      return (TypeAdapter<T>) new MethodEntityDTOAdapter();
    } else if (type.getRawType().equals(TypeParameterEntityDTO.class)) {
      return (TypeAdapter<T>) new TypeParameterEntityDTOAdapter();
    } else if (type.getRawType().equals(VariableEntityDTO.class)) {
      return (TypeAdapter<T>) new VariableEntityDTOAdapter();
    }
    return null;
  }
}
