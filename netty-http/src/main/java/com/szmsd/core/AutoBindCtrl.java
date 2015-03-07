package com.szmsd.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szmsd.anno.MsdCtrl;
import com.ttianjun.common.kit.Lists;
import com.ttianjun.common.kit.PathKit;
import com.ttianjun.common.kit.ref.ClassSearcher;

/**
 * 
 * @author TianJun
 * @Date 2015年3月2日
 * 自动绑定controller
 */
public class AutoBindCtrl {
	private static Logger log = LoggerFactory.getLogger(AutoBindCtrl.class);
	
	private List<String> includeJars = Lists.newArrayList();
    private boolean includeAllJarsInLib = false;
    private List<String> scanPackages = Lists.newArrayList();
    private String classpath = PathKit.getRootClassPath();
    private String libDir = "";//PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + "lib"
    public static List<MsdCtrlRef> msdCtrlRefList = Lists.newArrayList();
    /**
     * 添加需要扫描的包，默认为扫描所有包
     *
     * @param packages
     * @return
     */
    public AutoBindCtrl addScanPackages(String... packages) {
        for (String pkg : packages) {
            scanPackages.add(pkg);
        }
        return this;
    }
    public AutoBindCtrl addJars(String... jars) {
        if (jars != null) {
            for (String jar : jars) {
                includeJars.add(jar);
            }
        }
        return this;
    }
    
    public void bind(){
    	List<Class<?>> modelClasses = ClassSearcher.of(Object.class).libDir(libDir).classpath(classpath).injars(includeJars).includeAllJarsInLib(includeAllJarsInLib).search();
    	MsdCtrl msdCtrl;
    	for (Class<?> modelClass : modelClasses) {
    		msdCtrl = (MsdCtrl) modelClass.getAnnotation(MsdCtrl.class);
    		if(msdCtrl!=null){
    			msdCtrlRefList.add(new MsdCtrlRef(msdCtrl.url(),msdCtrl.method(), modelClass));
    			log.info("addMapping(" + (msdCtrl.url() + "," + modelClass.getName() + "," + msdCtrl.method() + ")"));
    		}
    	}
    }
}
