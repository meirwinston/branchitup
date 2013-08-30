package com.branchitup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.branchitup.export.EbookAttachmentCarrier;
import com.branchitup.export.ExportHandler;
import com.branchitup.persistence.StatelessDAO;
import com.branchitup.persistence.UserItemStatus;
import com.branchitup.persistence.entities.Attachment;
import com.branchitup.persistence.entities.PublishedBook;
import com.branchitup.persistence.entities.PublishedSheet;
import com.branchitup.persistence.entities.Publisher;
import com.branchitup.persistence.entities.UserName;
import com.branchitup.transfer.arguments.PublishedBookExportArgs;
import com.branchitup.transfer.arguments.PublishedBookExportArgs.PublisherExportArgs;

public class ScheduledServiceImpl  extends QuartzJobBean implements ScheduledService{
//	private JobDetail pdfJobDetail;
	private Logger logger = Logger.getLogger(getClass());
	private ExportHandler wkExporter = new ExportHandler();
	private SessionFactory sessionFactory;
	
	@Autowired
	private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;
	
//	@Autowired
//	private SchedulerFactoryBean schedulerFactoryBean;
	
//	private Scheduler scheduler;
	
	@Required
	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
		System.out.println("--->setSession.sessionFac: " + this.sessionFactory);
	}	
	public Session getSession(){
//		System.out.println("--->getSession.sessionFac: " + this.sessionFactory);
		Session s = null;
		try {
			s = this.sessionFactory.getCurrentSession();
		} 
		catch (Exception e) {
			s = this.sessionFactory.openSession();
		}
		return s;
	}
	
	@PostConstruct
	public void init(){
//		this.scheduler = this.schedulerFactoryBean.getObject();
//		try {
//			this.wkExporter = new WKExporter();
//			pdfJobDetail = new JobDetail("pdfJobDetail",ScheduledServiceImpl.class);
//			
//			//getSession() throws null pointer exception without the following line,
//			//probably calling this method from a different context!
//			pdfJobDetail.getJobDataMap().put("sessionFactory", this.sessionFactory);
//			this.scheduler.start();
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
	}
//	@Override
//	public void generatePDF(Long bookId) {
//		try {
//			schedulePDF(bookId);
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public void generatePDF(final Long bookId) {
		try {
			simpleAsyncTaskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					createAndStorePDF(bookId);
				}
			});
		} catch (Exception exp) {
			logger.error(exp.getMessage(),exp);
		}
	}
	
//	private void schedulePDF(Long bookId) throws SchedulerException{
////		Date date = new Date(System.currentTimeMillis() + Constants.PUBLISH_INTERVAL);
//		JobDataMap dataMap = new JobDataMap();
//		dataMap.put("bookId", bookId);
//		//the name will help us cancel the job if we want
//		String name = "PublishBook." + bookId;
//		Trigger trigger = new SimpleTrigger(name,new Date(System.currentTimeMillis() + Constants.PUBLISH_INTERVAL));
//		trigger.setJobDataMap(dataMap);
//		scheduler.scheduleJob(pdfJobDetail, trigger);
//		
////		System.out.println("----->schedulePDF started!!!" + HibernatePersistenceManager.findPublishedBook(session,p.getBookId()));
//	}
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Long bookId = (Long)context.getTrigger().getJobDataMap().get("bookId");
		createAndStorePDF(bookId);
	}
	
	private void createAndStorePDF(Long bookId){
		Session session = getSession();
		PublishedBook publication = null;
		Transaction transaction = session.beginTransaction();
		try {
			publication = StatelessDAO.findPublishedBook(session,bookId);
			
			if(publication != null){
				generateEBookAttachment(session, publication);
				StatelessDAO.setPublicationStatus(session, publication.getBookId(), UserItemStatus.ACTIVE);
			}
			transaction.commit();
			session.flush();
		}
		catch(Exception exp){
			logger.error("Error Generating PDF File",exp);
			transaction.rollback();
			
			if(publication != null){
				publication.setStatus(UserItemStatus.REMOVED);
			}
		}

	}
	
	public static void main(String[] args){
//		Exception e = new Exception();
//		e.printStackTrace();
		
//		Logger.getLogger(ScheduledServiceImpl.class).error(e);
//		Logger.getLogger(ScheduledServiceImpl.class).error("My Message",e);
	}
	
	private List<Attachment> generateEBookAttachment(Session session, PublishedBook pb) 
	throws Exception{
		List<Attachment> attachments = new ArrayList<Attachment>();
		
		PublishedBookExportArgs args = new PublishedBookExportArgs();
		args.title = pb.getTitle();
		args.bookId = pb.getBookId();
		
		args.publisherAccount = new PublishedBookExportArgs.PublisherExportArgs();
		args.publisherAccount.firstName = pb.getPublisherAccount().getFirstName();
		args.publisherAccount.lastName = pb.getPublisherAccount().getLastName();
		
		args.coverImageFile = pb.getCoverImageFile();
		args.sheetList = new ArrayList<PublishedBookExportArgs.SheetExportArgs>(pb.getSheetList().size());
		for(PublishedSheet ps : pb.getSheetList()){
			PublishedBookExportArgs.SheetExportArgs s = new PublishedBookExportArgs.SheetExportArgs();
			s.sheetId = ps.getSheetId();
			s.name = ps.getName();
			s.content = ps.getContent();
			s.cssText = ps.getCssText();
			
			args.sheetList.add(s);
		}
		Map<String, List<Publisher>> pmap = StatelessDAO.getContributorsMap(getSession(),pb.getBookId());
		
		args.contributorsByRole = new HashMap<String,List<PublishedBookExportArgs.PublisherExportArgs>>();
		if(pmap != null && pmap.size() > 0){
			Set<String> keys = pmap.keySet();
			for(String key : keys){
				List<Publisher> plist = pmap.get(key);
				List<PublisherExportArgs> publisherExportList = new ArrayList<PublisherExportArgs>(plist.size());
				for(Publisher publisher : plist){
					PublisherExportArgs publisherArgs = new PublisherExportArgs();
					publisherArgs.firstName = publisher.getFirstName();
					publisherArgs.lastName = publisher.getLastName();
					publisherExportList.add(publisherArgs);
				}
				
				if(publisherExportList.size() > 0){
					args.contributorsByRole.put(key, publisherExportList);
				}
			}
		}
		args.sheetOwners = new ArrayList<PublishedBookExportArgs.PublisherExportArgs>();
		List<UserName> sheetOwners = StatelessDAO.findSheetOwners(session, pb.getBookId());
		for(UserName userName : sheetOwners){
			PublisherExportArgs ownerArgs = new PublisherExportArgs();
			ownerArgs.firstName = userName.firstName;
			ownerArgs.lastName = userName.lastName;
			
			args.sheetOwners.add(ownerArgs);
		}
		List<EbookAttachmentCarrier> files = wkExporter.export(args);
		for(EbookAttachmentCarrier struct : files){
			Attachment att = StatelessDAO.insertAttachment(session, pb.getBookId(), struct.file, struct.type);
			if(att != null){
				attachments.add(att);
			}
		}
		return attachments;
	}

}
