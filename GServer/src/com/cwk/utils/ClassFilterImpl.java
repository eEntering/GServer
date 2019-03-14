package com.cwk.utils;

import java.lang.reflect.Modifier;

public class ClassFilterImpl implements ClassFilter {

	private Class<?>[] parents;
	
	public ClassFilterImpl(Class<?>[] parents) {
		this.parents = parents;
	}
	
	@Override
	public boolean filter(Class<?> clazz) {
		int modify = clazz.getModifiers();
		if (Modifier.isAbstract(modify) || Modifier.isInterface(modify)) {
			return false;
		}
		for (Class<?> parent : parents) {
			if (parent.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}
	
	public Class<?>[] getParents() {
		return parents;
	}
	public void setParents(Class<?>[] parents) {
		this.parents = parents;
	}
}
