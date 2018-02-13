package lu.kremi151.minamod.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import lu.kremi151.minamod.interfaces.TriFunction;

public class AnnotationProcessor<AnnotationType extends Annotation, ExpectedValue> {
	
	private final Class<AnnotationType> annotationClass;
	private final Class<ExpectedValue> expectedValueClass;
	
	public AnnotationProcessor(Class<AnnotationType> annotationClass, Class<ExpectedValue> expectedValueClass) {
		this.annotationClass = annotationClass;
		this.expectedValueClass = expectedValueClass;
	}

	public void process(Class clazz, BiConsumer<AnnotationType, ExpectedValue> callback, Predicate<ExpectedValue> predicate) {
		Field fields[] = clazz.getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			AnnotationType node = f.getAnnotation(annotationClass);
			if(node != null && expectedValueClass.isAssignableFrom(f.getType())){
				try {
					ExpectedValue val = (ExpectedValue) f.get(null);
					if(predicate.test(val)) {
						callback.accept(node, val);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public void process(Class clazz, BiConsumer<AnnotationType, ExpectedValue> callback) {
		process(clazz, callback, val -> true);
	}
	
	public void processStrict(Class clazz, BiConsumer<AnnotationType, ExpectedValue> callback) {
		process(clazz, callback, val -> val.getClass() == expectedValueClass);
	}

	public <Result> Result processWithResult(Class clazz, TriFunction<AnnotationType, ExpectedValue, Result, Result> callback, Result initialValue, Predicate<ExpectedValue> predicate) {
		Field fields[] = clazz.getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			AnnotationType node = f.getAnnotation(annotationClass);
			if(node != null && expectedValueClass.isAssignableFrom(f.getType())){
				try {
					ExpectedValue val = (ExpectedValue) f.get(null);
					if(predicate.test(val)) {
						initialValue = callback.apply(node, val, initialValue);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return initialValue;
	}

	public <Result> Result processWithResult(Class clazz, TriFunction<AnnotationType, ExpectedValue, Result, Result> callback, Result initialValue) {
		return processWithResult(clazz, callback, initialValue, val -> true);
	}

	public <Result> Result processStrictWithResult(Class clazz, TriFunction<AnnotationType, ExpectedValue, Result, Result> callback, Result initialValue) {
		return processWithResult(clazz, callback, initialValue, val -> val.getClass() == expectedValueClass);
	}
	
}
