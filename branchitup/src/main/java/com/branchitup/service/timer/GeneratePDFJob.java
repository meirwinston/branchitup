package com.branchitup.service.timer;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Deprecated
//@Service
public class GeneratePDFJob extends QuartzJobBean {
//	@Autowired
//	private HibernatePersistenceManager h;
	
//	@Autowired
//	public void setHibernatePersistenceManager(HibernatePersistenceManager h){
//		System.out.println("----------------->" + h);
//		this.h = h;
//	}
	
//	private Logger logger = Logger.getLogger(this.getClass()); 
	public void test(){
		System.out.println("GeneratePDFJob is now Executing!!!!");
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//		System.out.println("----->GeneratePDFJob.start!!!!" + 
//			context.getTrigger().getJobDataMap() + ", " + context.getJobDetail().getJobDataMap()
//		);
//		
////		Session session = (Session)context.getTrigger().getJobDataMap().get("session");
//		Long bookId = (Long)context.getTrigger().getJobDataMap().get("bookId");
////		HibernatePersistenceManager p = (HibernatePersistenceManager)context.getJobDetail().getJobDataMap().get("HibernatePersistenceManager");
//		SessionFactory sessionFactory = (SessionFactory)context.getJobDetail().getJobDataMap().get("SessionFactory");
//		
//		Session session = sessionFactory.openSession();
//		
//		System.out.println("----->GeneratePDFJob.executeInternal is now Executing!!!!" + bookId);
//		PublishedBook publication = null;
////		PublishedBook publication = p.findPublishedBook(publicationId);
//		
//		Transaction transaction = session.beginTransaction();
//		try {
//			System.out.println("TIMEOUT.before find." + bookId);
//			publication = StatelessDAO.findPublishedBook(session,bookId);
//			System.out.println("TIMEOUT.after find." + bookId);
//			if(publication != null){
//				System.out.println("----->TIMEOUT.start2." + publication);
//				StatelessDAO.generatePDFAttachment(session, publication);
//				System.out.println("----->TIMEOUT.after pdf." + publication.getBookId());
//				StatelessDAO.setPublicationStatus(session, publication.getBookId(), PublishedBook.Status.ACTIVE);
//			}
//			System.out.println("----->GeneratePDFJob.executeInternal. before commit");	
//			transaction.commit();
//			System.out.println("----->GeneratePDFJob.executeInternal. after commit");
////			System.out.println("TIMEOUT.set to active." + publication.getBookId());
//			session.flush();
//			session.close();
//		}
//		catch(Exception exp){
//			exp.printStackTrace();
////			logger.trace(exp);
//			transaction.rollback();
//			
//			if(publication != null){
//				publication.setStatus(PublishedBook.Status.REMOVED);
//			}
//		}
	}
}

/*
 
@Timeout
protected void timeout(Timer timer) {
	System.out.println("TIMEOUT.start");
	Hashtable<String,Object> params = uncheckedCast(timer.getInfo());
	if(params.get("command").equals(TimerCommand.GENERATE_PDF_ATTACHMENT)){
//		this.pHandler.getPublicationRecord(bookId);
		
		PublishedBook p = this.pHandler.findPublishedBook((long)params.get("publicationId"));//(PublishedBook)params.get("publication");
		System.out.println("TIMEOUT.start." + p.getBookId());
		try {
			System.out.println("TIMEOUT.before pdf." + p.getBookId());
			transaction.begin();
			this.pHandler.generatePDFAttachment(p);
			System.out.println("TIMEOUT.after pdf." + p.getBookId());
			this.pHandler.setPublicationStatus(p.getBookId(), PublishedBook.Status.ACTIVE);
			transaction.commit();
			System.out.println("TIMEOUT.set to active." + p.getBookId());
		} 
		catch (IOException | InterruptedException | NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException exp) {
			Logger.log(exp);
			p.setStatus(PublishedBook.Status.REMOVED);
		} 
	}
}
*/