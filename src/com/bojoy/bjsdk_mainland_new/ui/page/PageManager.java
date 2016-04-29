package com.bojoy.bjsdk_mainland_new.ui.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.page.base.ViewPage;
import com.bojoy.bjsdk_mainland_new.utils.PageThread;

import java.util.Stack;

/**
 * @author xuxiaobin
 * PageManager ViewPage管理器
 */
public class PageManager {
	
	@SuppressWarnings("unused")
	private final String TAG = PageManager.class.getSimpleName();
	
	private ViewGroup root;
	private ViewPage currentPage = null;
	private Stack<ViewPage> pageStack;
	private int width, height;
	private PageThread pageThread = null;

	public PageManager() {
		pageStack = new Stack<ViewPage>();
	}
	
	public PageManager(ViewGroup root) {
		pageStack = new Stack<ViewPage>();
		this.root = root;
	}

	public ViewGroup getRoot() {
		return root;
	}

	public void setRoot(ViewGroup root) {
		this.root = root;
	}

	public ViewPage getCurrentPage() {
		return currentPage;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 添加新页面
	 * @param page
	 */
	public void addPage(ViewPage page, String... tag) {
		addPage(page, false, tag);
	}
	
	/**
	 * 新页面替换老页面
	 * @param page
	 */
	public void replacePage(ViewPage page, String... tag) {
		addPage(page, true, tag);
	}
	
	private void addPage(ViewPage page, boolean direct, String... tag) {
		if (root == null) {
			return;
		}

		onDetachPage();
		if (!direct) {
			if (currentPage != null) {
				pageStack.push(currentPage);
				if(tag.length != 0){
					currentPage.setTag(tag[0]);
				}
			}
		}
		currentPage = page;
		root.removeAllViews();
		page.onAttach(root);
		if (width == 0 || height == 0) {
			root.addView(page.getPageView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			root.addView(page.getPageView(), new LayoutParams(width, height));
		}
	}


	
	/**
	 * 将页面堆栈清理后添加新页面
	 * @param page
	 */
	public void clearTopPage(ViewPage page, String... tag) {
		clear();
		addPage(page, true, tag);
	}
	
	/**
	 * 清空页面的堆栈
	 */
	public void clear() {
		pageStack.clear();
	}
	
	public void clean() {
		onDetachPage();
		clear();
		currentPage = null;
	}
	
	private void onDetachPage() {
		if (currentPage != null) {
			currentPage.onDetach();
		}
	}
	
	/**
	 * 返回上一个页面
	 * @return
	 */
	public boolean previousPage(String... tag) {
		if (pageStack.empty()) {
			return false;
		}
		ViewPage page = pageStack.pop();
		if (page == null) {
			return false;
		}
		addPage(page, true, tag);
		return true;
	}
	
	/**
	 * 取得上一个页面对象
	 * @return
	 */
	public ViewPage getPreviousPage() {
		if (pageStack.empty()) {
			return null;
		}
		ViewPage page = pageStack.get(pageCount()-1);
		if (page == null) {
			return null;
		}
		return page;
	}
	
	/**
	 * 返回到某一个页面
	 * @param backPage
	 * @return
	 */
	public boolean backToPage(ViewPage backPage, String... tag) {
		if (pageStack.empty()) {
			return false;
		}
		while (true) {
			if (pageStack.empty()) {
				break;
			}
			ViewPage page = pageStack.pop();
			if (page == null) {
				continue;
			}
			if (page.equals(backPage)) {
				addPage(page, true, tag);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 返回到顶层页面
	 */
	public boolean backToTopPage(String... tag) {
		if (pageStack.empty()) {
			return false;
		}
		while (true) {
			ViewPage page = pageStack.pop();
			if (pageStack.empty()) {
				if (page != null) {
					addPage(page, true, tag);
					return true;
				}
				return false;
			}
		}
	}
	
	public int pageCount() {
		return pageStack.size();
	}
	
	public Stack<ViewPage> getStack(){
		return pageStack;
	}
	
	public ViewPage getViewPage(String tag){
		for(int i = 0; i < pageStack.size(); i++){
			if(pageStack.get(i).getTag().equals(tag)){
				return pageStack.get(i);
			}
		}
		return null;
	}

	/**
	 * 带标志打开view，没有传递参数
	 * @param currentPage
	 * @param targetPage
	 * @param sign
     */
	public void addViewForResult(BaseActivityPage currentPage, BaseActivityPage targetPage, int sign) {
		addViewForResult(currentPage, targetPage, sign, null);
	}

	/**
	 * 带标志打开view，有传递参数
	 * @param currentPage
	 * @param targetPage
	 * @param sign
     * @param bundle
     */
	public void addViewForResult(BaseActivityPage currentPage, BaseActivityPage targetPage, int sign, @Nullable Bundle bundle) {
		if (targetPage == null) {
			throw new NullPointerException();
		}
		pageThread = new PageThread();
		currentPage.addListener();
		targetPage.addListener();
		pageThread.attach(targetPage, sign);
		targetPage.setBundle(bundle);
		addPage(targetPage);
	}

	public PageThread getPageThread() {
		return pageThread;
	}

	public void clearPageThread() {
		pageThread = null;
	}
}
