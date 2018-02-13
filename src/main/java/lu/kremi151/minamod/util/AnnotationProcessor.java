package lu.kremi151.minamod.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class AnnotationProcessor<AnnotationType extends Annotation, ExpectedValue> {
	
	private final Class<AnnotationType> annotationClass;
	private final Class<ExpectedValue> expectedValueClass;
	
	public AnnotationProcessor(Class<AnnotationType> annotationClass, Class<ExpectedValue> expectedValueClass) {
		this.annotationClass = annotationClass;
		this.expectedValueClass = expectedValueClass;
	}

	public void process(Class clazz, BiConsumer<AnnotationType, ExpectedValue> callback) {
		Field fields[] = clazz.getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			AnnotationType node = f.getAnnotation(annotationClass);
			if(node != null && f.getType() == expectedValueClass){
				try {
					callback.accept(node, (ExpectedValue) f.get(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
