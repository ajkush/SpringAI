# Documentation Index

Welcome to the Weather App documentation! This guide will help you find the right documentation for your needs.

## 🚀 Getting Started (New Users)

**Start here if this is your first time:**

1. **[README.md](README.md)** - Overview and quick start
2. **[QUICKSTART_GUIDE.md](QUICKSTART_GUIDE.md)** - Fast 5-minute setup
3. **[GETTING_STARTED.md](GETTING_STARTED.md)** - Detailed walkthrough

## 🔑 API Keys Setup

**Need help with API keys?**

- **[API_KEYS_GUIDE.md](API_KEYS_GUIDE.md)** - Complete guide for OpenWeatherMap and OpenAI API keys
  - Which keys are required vs optional
  - Cost breakdown (FREE vs PAID)
  - Step-by-step setup instructions
  - Troubleshooting common issues
  - Security best practices

**Quick Facts:**
- ✅ **OpenWeatherMap** - Required, 100% FREE
- ⚠️ **OpenAI** - Optional, requires $5 minimum payment

## 💻 Using the Application

**How to use the features:**

- **[WEB_INTERFACE_GUIDE.md](WEB_INTERFACE_GUIDE.md)** - Interactive web UI with world map
- **[test-api.sh](test-api.sh)** - Script to test all API endpoints
- Access Swagger UI at: http://localhost:8080/swagger-ui.html

## 📝 Reference Documentation

**Technical details and updates:**

- **[DOCS_UPDATE_SUMMARY.md](DOCS_UPDATE_SUMMARY.md)** - Recent documentation changes
- **[HELP.md](HELP.md)** - Spring Boot reference documentation
- **[pom.xml](pom.xml)** - Maven dependencies and configuration

## 🎯 Common Tasks

### I want to run the app locally
1. Read [QUICKSTART_GUIDE.md](QUICKSTART_GUIDE.md)
2. Get free OpenWeatherMap API key from [API_KEYS_GUIDE.md](API_KEYS_GUIDE.md)
3. Run: `./run-app.sh`
4. Open: http://localhost:8080

### I want to understand API keys
1. Read [API_KEYS_GUIDE.md](API_KEYS_GUIDE.md)
2. Understand the difference between FREE and PAID options
3. Decide if you need OpenAI (probably not!)

### I want to use the web interface
1. Start the app: `./run-app.sh`
2. Read [WEB_INTERFACE_GUIDE.md](WEB_INTERFACE_GUIDE.md)
3. Open browser: http://localhost:8080

### I want to test the API endpoints
1. Start the app
2. Run: `./test-api.sh`
3. Or visit: http://localhost:8080/swagger-ui.html

### I want to enable AI features
1. Read [API_KEYS_GUIDE.md](API_KEYS_GUIDE.md) - OpenAI section
2. Understand you need $5 minimum payment
3. Get OpenAI API key
4. Set `OPENAI_API_KEY` environment variable
5. Restart app

## 🆘 Troubleshooting

### Common Issues

**"Invalid API key" error:**
- Check [API_KEYS_GUIDE.md](API_KEYS_GUIDE.md) - Troubleshooting section
- Verify you set `OPENWEATHER_API_KEY`
- Wait 10-15 minutes after creating key

**"Port 8080 already in use":**
```bash
lsof -ti:8080 | xargs kill -9
./run-app.sh
```

**OpenAI features not working:**
- Check if you set `OPENAI_API_KEY`
- Verify you have $5+ balance in OpenAI account
- Remember: app works fully without OpenAI!

**Web UI map not loading:**
- Check browser console for errors
- Ensure app is fully started (wait 30 seconds)
- Try refreshing the page

## 📊 What Works Without OpenAI

**100% FREE Features:**
- ✅ Complete weather data
- ✅ Search by city name
- ✅ Search by GPS coordinates
- ✅ Interactive world map
- ✅ Raw weather endpoint
- ✅ Quick city buttons
- ✅ Caching (10 minutes)
- ✅ Error handling

**PAID Features (OpenAI required):**
- ⚠️ AI weather summaries
- ⚠️ Activity recommendations
- ⚠️ Conversational chat

**Bottom Line:** You don't need OpenAI to use this app!

## 🔄 Quick Reference

| Task | Document | Time |
|------|----------|------|
| First time setup | QUICKSTART_GUIDE.md | 5 min |
| Get API keys | API_KEYS_GUIDE.md | 10 min |
| Use web interface | WEB_INTERFACE_GUIDE.md | 2 min |
| Test API endpoints | test-api.sh | 1 min |
| Detailed guide | GETTING_STARTED.md | 15 min |

## 📖 Documentation Status

**Last Updated:** November 2, 2025

All documentation is up-to-date and accurate:
- ✅ OpenAI marked as optional and paid
- ✅ OpenWeatherMap marked as required and free
- ✅ No misleading "free OpenAI" references
- ✅ Clear cost breakdowns
- ✅ Accurate as of November 2025

## 🎓 Learning Path

**Complete beginner?** Follow this path:

1. Start: [README.md](README.md)
2. Setup: [QUICKSTART_GUIDE.md](QUICKSTART_GUIDE.md)
3. API Keys: [API_KEYS_GUIDE.md](API_KEYS_GUIDE.md)
4. Use: [WEB_INTERFACE_GUIDE.md](WEB_INTERFACE_GUIDE.md)
5. Explore: Test different features via web UI

**Time required:** ~30 minutes to be fully productive

## 💡 Pro Tips

1. **Start FREE:** Use only OpenWeatherMap API - it's all you need
2. **Skip OpenAI:** Unless you really want AI summaries (costs money)
3. **Use the web UI:** Much easier than API calls
4. **Check caching:** Weather data is cached for 10 minutes
5. **Read error messages:** They're detailed and helpful

## 📞 Need More Help?

1. Check the relevant guide above
2. Look for error messages in terminal
3. Verify API keys are set: `echo $OPENWEATHER_API_KEY`
4. Review [API_KEYS_GUIDE.md](API_KEYS_GUIDE.md) troubleshooting section

---

**Happy weather checking! 🌤️**

*Remember: This app is 100% functional for FREE - OpenAI is completely optional!*

