package com.sappress.hcpbook.chp03;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ReadingListManagerBeanLocal
{
  public void addTitle(String title);
  public boolean removeTitle(String title);
  public int getCount();
  public List<String> getReadingList();
}